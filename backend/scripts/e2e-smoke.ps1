$ErrorActionPreference = "Stop"

function Invoke-Json([string]$Method, [string]$Url, $Body = $null, [hashtable]$Headers = @{}) {
  if ($null -ne $Body) {
    $json = ($Body | ConvertTo-Json -Compress)
    return Invoke-RestMethod -Uri $Url -Method $Method -ContentType "application/json" -Headers $Headers -Body $json
  }
  return Invoke-RestMethod -Uri $Url -Method $Method -Headers $Headers
}

function Step([string]$Name, [scriptblock]$Action) {
  try {
    return & $Action
  } catch {
    Write-Error ("Step failed: {0}`n{1}" -f $Name, $_.Exception.Message)
    throw
  }
}

function Login([string]$Email, [string]$Password) {
  $res = Invoke-Json "Post" "http://localhost:8080/api/auth/login" @{ email=$Email; password=$Password }
  if (-not $res.success) { throw "Login failed" }
  return @{ token=$res.data.token; user=$res.data.user }
}

# Admin login
$admin = Step "Admin login" { Login "admin@gmail.com" "admin123" }
$adminHeaders = @{ Authorization = "Bearer $($admin.token)" }

# Ensure products exist and get one productId
$products = Step "Get products" { Invoke-Json "Get" "http://localhost:8080/api/products" }
$productId = $products.data[0].id

# Customer register + login
$u = "cust_{0}" -f (Get-Random)
$email = "$u@example.com"
$reg = Step "Customer register" { Invoke-Json "Post" "http://localhost:8080/api/auth/register" @{ name="Customer"; email=$email; password="password"; phone="9999999999" } }
$cust = Step "Customer login" { Login $email "password" }
$custHeaders = @{ Authorization = "Bearer $($cust.token)" }
$userId = $cust.user.id

# Add to cart
$add = Step "Add to cart" { Invoke-Json "Post" "http://localhost:8080/api/cart/add?userId=$userId" @{ productId=$productId; quantity=2 } $custHeaders }

# Apply coupon
$couponApply = Step "Apply coupon" { Invoke-Json "Post" "http://localhost:8080/api/coupons/apply?code=SAVE10&orderAmount=500" $null $custHeaders }

# Place order with coupon (order uses JWT principal)
$order = Step "Place order" { Invoke-Json "Post" "http://localhost:8080/api/orders" @{ paymentMethod="COD"; couponCode="SAVE10" } $custHeaders }
$orderId = $order.data.id

# Admin list orders and update status
$adminOrders = Step "Admin get orders" { Invoke-Json "Get" "http://localhost:8080/api/orders/admin" $null $adminHeaders }
$statusUpdate = Step "Admin update status" { Invoke-Json "Patch" "http://localhost:8080/api/orders/$orderId/status?status=PACKED" $null $adminHeaders }

# Customer fetch orders
$custOrders = Step "Customer get orders" { Invoke-Json "Get" "http://localhost:8080/api/orders" $null $custHeaders }

@{
  adminRole = $admin.user.role
  productsCount = $products.data.Count
  customerId = $userId
  cartAddSuccess = $add.success
  couponApplySuccess = $couponApply.success
  placedOrderId = $orderId
  adminOrdersCount = $adminOrders.data.Count
  updatedStatus = $statusUpdate.data.status
  customerOrdersCount = $custOrders.data.Count
} | ConvertTo-Json -Compress

