$ErrorActionPreference = "Stop"

function Invoke-Json([string]$Method, [string]$Url, $Body = $null, [hashtable]$Headers = @{}) {
  if ($null -ne $Body) {
    $json = ($Body | ConvertTo-Json -Compress)
    return Invoke-RestMethod -Uri $Url -Method $Method -ContentType "application/json" -Headers $Headers -Body $json
  }
  return Invoke-RestMethod -Uri $Url -Method $Method -Headers $Headers
}

# Helper to pinpoint failures in CI/console runs
function Step([string]$Name, [scriptblock]$Action) {
  try { return & $Action }
  catch {
    Write-Error ("Step failed: {0}`n{1}" -f $Name, $_.Exception.Message)
    throw
  }
}

# 1) Seed a product (API currently allows without auth)
$productCreate = @{
  name = "Test Pizza"
  description = "Cheese"
  price = 9.99
  stock = 10
  size = "M"
  imageUrl = ""
  rating = 4.5
  isAvailable = $true
  category = "Pizza"
}
$productResp = Step "Create product" { Invoke-Json "Post" "http://localhost:8080/api/products" $productCreate }
$productId = $productResp.data.id

# 2) Register + login
$u = "flow_{0}" -f (Get-Random)
$email = "$u@example.com"
$regBody = @{ name="Flow User"; email=$email; password="password"; phone="9999999999" }
$regResp = Step "Register" { Invoke-Json "Post" "http://localhost:8080/api/auth/register" $regBody }
$loginBody = @{ email=$email; password="password" }
$loginResp = Step "Login" { Invoke-Json "Post" "http://localhost:8080/api/auth/login" $loginBody }
$token = $loginResp.data.token
$userId = $loginResp.data.user.id
$authHeaders = @{ Authorization = "Bearer $token" }

# 3) Cart add -> get
$addBody = @{ productId=$productId; quantity=2 }
$addCartResp = Step "Add to cart" { Invoke-Json "Post" "http://localhost:8080/api/cart/add?userId=$userId" $addBody $authHeaders }
$getCartResp = Step "Get cart" { Invoke-Json "Get" "http://localhost:8080/api/cart?userId=$userId" $null $authHeaders }

# 4) Place order
$orderBody = @{ paymentMethod="COD" }
$orderResp = Step "Place order" { Invoke-Json "Post" "http://localhost:8080/api/orders" $orderBody $authHeaders }

@{
  productId = $productId
  regSuccess = $regResp.success
  loginSuccess = $loginResp.success
  cartAddSuccess = $addCartResp.success
  cartGetSuccess = $getCartResp.success
  orderSuccess = $orderResp.success
  orderId = $orderResp.data.id
} | ConvertTo-Json -Compress

