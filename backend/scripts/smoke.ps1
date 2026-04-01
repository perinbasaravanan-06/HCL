$ErrorActionPreference = "Stop"

function Invoke-JsonPost([string]$Url, [hashtable]$Body, [hashtable]$Headers = @{}) {
  $json = ($Body | ConvertTo-Json -Compress)
  return Invoke-RestMethod -Uri $Url -Method Post -ContentType "application/json" -Headers $Headers -Body $json
}

function Invoke-JsonGet([string]$Url, [hashtable]$Headers = @{}) {
  return Invoke-RestMethod -Uri $Url -Method Get -Headers $Headers
}

$u = "testuser_{0}" -f (Get-Random)
$p = "password"
$email = "$u@example.com"

$reg = Invoke-JsonPost "http://localhost:8080/api/auth/register" @{
  name = "Test User"
  email = $email
  password = $p
  phone = "9999999999"
}

$login = Invoke-JsonPost "http://localhost:8080/api/auth/login" @{
  email = $email
  password = $p
}

$token = $login.data.token
$userId = $login.data.user.id
$headers = @{ Authorization = "Bearer $token" }

$products = Invoke-JsonGet "http://localhost:8080/api/products"

# Cart endpoints in this backend currently require userId as query param.
$cart = Invoke-JsonGet "http://localhost:8080/api/cart?userId=$userId" $headers

@{
  user = $u
  regSuccess = $reg.success
  loginSuccess = $login.success
  tokenLen = $token.Length
  userId = $userId
  productsSuccess = $products.success
  cartSuccess = $cart.success
} | ConvertTo-Json -Compress

