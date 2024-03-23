package com.faddy.vpnsdk

object VpnConfigs {
    val openVpnConf =
        "Y2xpZW50CnByb3RvIHVkcApleHBsaWNpdC1leGl0LW5vdGlmeQpyZW1vdGUgMTY1LjIzMS4zNC42MCAxMTk0CmRldiB0dW4KcmVzb2x2LXJldHJ5IGluZmluaXRlCm5vYmluZApwZXJzaXN0LWtleQpwZXJzaXN0LXR1bgpyZW1vdGUtY2VydC10bHMgc2VydmVyCnZlcmlmeS14NTA5LW5hbWUgc2VydmVyX2p6WW9lNnlHUExnNWlyR0YgbmFtZQphdXRoIFNIQTI1NgphdXRoLW5vY2FjaGUKYXV0aC11c2VyLXBhc3MKZGhjcC1vcHRpb24gRE5TIDguOC44LjgKZGhjcC1vcHRpb24gRE9NQUlOIGdvb2dsZS5jb20KY2lwaGVyICBBRVMtMTI4LUdDTQp0bHMtY2xpZW50CnRscy12ZXJzaW9uLW1pbiAxLjIKdGxzLWNpcGhlciBUTFMtRUNESEUtRUNEU0EtV0lUSC1BRVMtMTI4LUdDTS1TSEEyNTYKaWdub3JlLXVua25vd24tb3B0aW9uIGJsb2NrLW91dHNpZGUtZG5zCnNldGVudiBvcHQgYmxvY2stb3V0c2lkZS1kbnMgIyBQcmV2ZW50IFdpbmRvd3MgMTAgRE5TIGxlYWsKc2V0ZW52IENMSUVOVF9DRVJUIDAKdmVyYiAzCjxjYT4KLS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUJ3VENDQVdlZ0F3SUJBZ0lKQU55NmhvWTZSNmI4TUFvR0NDcUdTTTQ5QkFNQ01CNHhIREFhQmdOVkJBTU0KRTJOdVgzazBZbEJ2WW1jM1NrdEpia0ZUWW5Nd0hoY05NalF3TWpJd01USXlNelEyV2hjTk16UXdNakUzTVRJeQpNelEyV2pBZU1Sd3dHZ1lEVlFRRERCTmpibDk1TkdKUWIySm5OMHBMU1c1QlUySnpNRmt3RXdZSEtvWkl6ajBDCkFRWUlLb1pJemowREFRY0RRZ0FFNzhuMHNtSFhZdGJpUmZDdFQzTklxRk4xTlBXdjN2b1lxb0dOVWN3TUNsYVkKS3h2NWxOcmJBODZIWERKcWpFMzFNMHNnbDVDSm9VYmY5TU5Db3ZsSnVxT0JqVENCaWpBTUJnTlZIUk1FQlRBRApBUUgvTUIwR0ExVWREZ1FXQkJSVTdNRzRjNEZlZHRsaXFtdytEU2hETW1QaEN6Qk9CZ05WSFNNRVJ6QkZnQlJVCjdNRzRjNEZlZHRsaXFtdytEU2hETW1QaEM2RWlwQ0F3SGpFY01Cb0dBMVVFQXd3VFkyNWZlVFJpVUc5aVp6ZEsKUzBsdVFWTmljNElKQU55NmhvWTZSNmI4TUFzR0ExVWREd1FFQXdJQkJqQUtCZ2dxaGtqT1BRUURBZ05JQURCRgpBaUIzc0hrckt3c3d2RW1xVGlXMi9kYkJoYW9rTXQ5ZnBHNWtzRTVGWkxocitRSWhBUDRheGxVUzRXUndMdVBMClZOSm1tRGNWTnhBM09VcytoZlZndVRBd2Y1enYKLS0tLS1FTkQgQ0VSVElGSUNBVEUtLS0tLQo8L2NhPgo8dGxzLWNyeXB0PgojCiMgMjA0OCBiaXQgT3BlblZQTiBzdGF0aWMga2V5CiMKLS0tLS1CRUdJTiBPcGVuVlBOIFN0YXRpYyBrZXkgVjEtLS0tLQpiMjExMzQ0NGRkZGE1MzY3NDkzNzAwNzQzNzE3ZTIxMQplNTUyNTBlYmQ2ZTQzZjYwNWU2NjBiZGZlOGIwMzYwYQo3ZmVkOWUyZmU1YTZmNWVlMzQxYmNhMzkwOWYxNTFiYwpkYzEzODA0Mzk0MzdlNmJkMWIzMzI1OGQ0N2ZhNDk0NQo4ZGY3ZGNiMjZhNTI1NTcwZDE2ZTA1YmVlMWYyOWNkNgoyNzA2MjBmYTI4YjExNzRiYThkYTViODBkODRkMmViNwo4MzdkNjJjYTFiMDVlOGExNmFlNjJjYjAzMTA4YWE0OQphYjc5NDQxZTYwOGE3NGVlNzRlNjliMThhZTdjNTBmNwo0NGM2MWU3YzE3MzM4YTYzNzE4ZTAwZjkxNDhhNTJiNwo3MDM1ZjEwYzdlN2UxZmI2ZGQ5ZTEzYTA2MzU1ZjAyMAo4Mzk0ZDAwYTZjMWUzNzcyY2YwMWI3NTA5Y2U0ZTMyYgpkOTNkYzE1MzQ5MmQ2ZmUyNWQ1NTJlZmEyNzY3MTc0NAoyZGZhNTQ0YjEwNGY4MGI5MGJlZTVjNmVlMTI3ZDU1MAo0ZmJjZDg1Njg5YzU3YTlmMDQwNGFlMzc5YjJlMWFmZQphMDZhZTdlNmM4NTg0NDFlNzYwNDdjM2U1MThiODg4NgplMDNkYjNhNTY3M2UxZmQwZDQxODBlNjVhZDRiNWI1ZAotLS0tLUVORCBPcGVuVlBOIFN0YXRpYyBrZXkgVjEtLS0tLQo8L3Rscy1jcnlwdD4K"
    val openVpnIP = "165.231.34.60:1194"
    val wgTunnelConfig =
        "[Interface]\nAddress = 10.7.19.4/32\nDNS = 8.8.8.8, 8.8.4.4\nPrivateKey = OK8m04WBsQvuq0Tb3zj7ZAxLkeVTrprZedHaUrTdRFU=\n\n[Peer]\nPublicKey = bdOcBvDqwdykOi5A1fC2VnROJNhv3+iw0dTkgx5jPQk=\nPresharedKey = cMy4mUafmwJ4+Z5LGuHGFzAYY0FYrMUyHOjEu4/k0pI=\nAllowedIPs = 0.0.0.0/0, ::/0\nEndpoint = 134.122.54.172:51820\nPersistentKeepalive = 25"

}