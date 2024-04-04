package com.faddy.vpnsdk

object VpnConfigs {
    val openVpnConf =
        "Y2xpZW50CnByb3RvIHVkcApleHBsaWNpdC1leGl0LW5vdGlmeQpyZW1vdGUgMTY1LjIzMS4zNC42MCAxMTk0CmRldiB0dW4KcmVzb2x2LXJldHJ5IGluZmluaXRlCm5vYmluZApwZXJzaXN0LWtleQpwZXJzaXN0LXR1bgpyZW1vdGUtY2VydC10bHMgc2VydmVyCnZlcmlmeS14NTA5LW5hbWUgc2VydmVyX2p6WW9lNnlHUExnNWlyR0YgbmFtZQphdXRoIFNIQTI1NgphdXRoLW5vY2FjaGUKYXV0aC11c2VyLXBhc3MKZGhjcC1vcHRpb24gRE5TIDguOC44LjgKZGhjcC1vcHRpb24gRE9NQUlOIGdvb2dsZS5jb20KY2lwaGVyICBBRVMtMTI4LUdDTQp0bHMtY2xpZW50CnRscy12ZXJzaW9uLW1pbiAxLjIKdGxzLWNpcGhlciBUTFMtRUNESEUtRUNEU0EtV0lUSC1BRVMtMTI4LUdDTS1TSEEyNTYKaWdub3JlLXVua25vd24tb3B0aW9uIGJsb2NrLW91dHNpZGUtZG5zCnNldGVudiBvcHQgYmxvY2stb3V0c2lkZS1kbnMgIyBQcmV2ZW50IFdpbmRvd3MgMTAgRE5TIGxlYWsKc2V0ZW52IENMSUVOVF9DRVJUIDAKdmVyYiAzCjxjYT4KLS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSUJ3VENDQVdlZ0F3SUJBZ0lKQU55NmhvWTZSNmI4TUFvR0NDcUdTTTQ5QkFNQ01CNHhIREFhQmdOVkJBTU0KRTJOdVgzazBZbEJ2WW1jM1NrdEpia0ZUWW5Nd0hoY05NalF3TWpJd01USXlNelEyV2hjTk16UXdNakUzTVRJeQpNelEyV2pBZU1Sd3dHZ1lEVlFRRERCTmpibDk1TkdKUWIySm5OMHBMU1c1QlUySnpNRmt3RXdZSEtvWkl6ajBDCkFRWUlLb1pJemowREFRY0RRZ0FFNzhuMHNtSFhZdGJpUmZDdFQzTklxRk4xTlBXdjN2b1lxb0dOVWN3TUNsYVkKS3h2NWxOcmJBODZIWERKcWpFMzFNMHNnbDVDSm9VYmY5TU5Db3ZsSnVxT0JqVENCaWpBTUJnTlZIUk1FQlRBRApBUUgvTUIwR0ExVWREZ1FXQkJSVTdNRzRjNEZlZHRsaXFtdytEU2hETW1QaEN6Qk9CZ05WSFNNRVJ6QkZnQlJVCjdNRzRjNEZlZHRsaXFtdytEU2hETW1QaEM2RWlwQ0F3SGpFY01Cb0dBMVVFQXd3VFkyNWZlVFJpVUc5aVp6ZEsKUzBsdVFWTmljNElKQU55NmhvWTZSNmI4TUFzR0ExVWREd1FFQXdJQkJqQUtCZ2dxaGtqT1BRUURBZ05JQURCRgpBaUIzc0hrckt3c3d2RW1xVGlXMi9kYkJoYW9rTXQ5ZnBHNWtzRTVGWkxocitRSWhBUDRheGxVUzRXUndMdVBMClZOSm1tRGNWTnhBM09VcytoZlZndVRBd2Y1enYKLS0tLS1FTkQgQ0VSVElGSUNBVEUtLS0tLQo8L2NhPgo8dGxzLWNyeXB0PgojCiMgMjA0OCBiaXQgT3BlblZQTiBzdGF0aWMga2V5CiMKLS0tLS1CRUdJTiBPcGVuVlBOIFN0YXRpYyBrZXkgVjEtLS0tLQpiMjExMzQ0NGRkZGE1MzY3NDkzNzAwNzQzNzE3ZTIxMQplNTUyNTBlYmQ2ZTQzZjYwNWU2NjBiZGZlOGIwMzYwYQo3ZmVkOWUyZmU1YTZmNWVlMzQxYmNhMzkwOWYxNTFiYwpkYzEzODA0Mzk0MzdlNmJkMWIzMzI1OGQ0N2ZhNDk0NQo4ZGY3ZGNiMjZhNTI1NTcwZDE2ZTA1YmVlMWYyOWNkNgoyNzA2MjBmYTI4YjExNzRiYThkYTViODBkODRkMmViNwo4MzdkNjJjYTFiMDVlOGExNmFlNjJjYjAzMTA4YWE0OQphYjc5NDQxZTYwOGE3NGVlNzRlNjliMThhZTdjNTBmNwo0NGM2MWU3YzE3MzM4YTYzNzE4ZTAwZjkxNDhhNTJiNwo3MDM1ZjEwYzdlN2UxZmI2ZGQ5ZTEzYTA2MzU1ZjAyMAo4Mzk0ZDAwYTZjMWUzNzcyY2YwMWI3NTA5Y2U0ZTMyYgpkOTNkYzE1MzQ5MmQ2ZmUyNWQ1NTJlZmEyNzY3MTc0NAoyZGZhNTQ0YjEwNGY4MGI5MGJlZTVjNmVlMTI3ZDU1MAo0ZmJjZDg1Njg5YzU3YTlmMDQwNGFlMzc5YjJlMWFmZQphMDZhZTdlNmM4NTg0NDFlNzYwNDdjM2U1MThiODg4NgplMDNkYjNhNTY3M2UxZmQwZDQxODBlNjVhZDRiNWI1ZAotLS0tLUVORCBPcGVuVlBOIFN0YXRpYyBrZXkgVjEtLS0tLQo8L3Rscy1jcnlwdD4K"
    val openVpnIP = "165.231.34.60:1194"
    val wgTunnelConfig =
        "[Interface]\nAddress = 10.7.19.4/32\nDNS = 8.8.8.8, 8.8.4.4\nPrivateKey = OK8m04WBsQvuq0Tb3zj7ZAxLkeVTrprZedHaUrTdRFU=\n\n[Peer]\nPublicKey = bdOcBvDqwdykOi5A1fC2VnROJNhv3+iw0dTkgx5jPQk=\nPresharedKey = cMy4mUafmwJ4+Z5LGuHGFzAYY0FYrMUyHOjEu4/k0pI=\nAllowedIPs = 0.0.0.0/0, ::/0\nEndpoint = 134.122.54.172:51820\nPersistentKeepalive = 25"
    val singBoxConfigEnc =
        "ewogICAgImxvZyI6IHsKICAgICAgICAiZGlzYWJsZWQiOiBmYWxzZSwKICAgICAgICAibGV2ZWwiOiAid2FybiIsCiAgICAgICAgInRpbWVzdGFtcCI6IHRydWUKICAgIH0sCiAgICAiZG5zIjogewogICAgICAgICJzZXJ2ZXJzIjogWwogICAgICAgICAgICB7CiAgICAgICAgICAgICAgICAidGFnIjogImRuc19ibG9jayIsCiAgICAgICAgICAgICAgICAiYWRkcmVzcyI6ICJyY29kZTovL3N1Y2Nlc3MiCiAgICAgICAgICAgIH0sCiAgICAgICAgICAgIHsKICAgICAgICAgICAgICAgICJ0YWciOiAiZG5zX2Zha2VpcCIsCiAgICAgICAgICAgICAgICAiYWRkcmVzcyI6ICJmYWtlaXAiCiAgICAgICAgICAgIH0sCiAgICAgICAgICAgIHsKICAgICAgICAgICAgICAgICJ0YWciOiAiZG5zX2xvY2FsIiwKICAgICAgICAgICAgICAgICJhZGRyZXNzIjogIjIyMy41LjUuNSIsCiAgICAgICAgICAgICAgICAiZGV0b3VyIjogImRpcmVjdCIKICAgICAgICAgICAgfQogICAgICAgIF0sCiAgICAgICAgInJ1bGVzIjogWwogICAgICAgICAgICB7CiAgICAgICAgICAgICAgICAib3V0Ym91bmQiOiAiYW55IiwKICAgICAgICAgICAgICAgICJzZXJ2ZXIiOiAiZG5zX2xvY2FsIgogICAgICAgICAgICB9LAogICAgICAgICAgICB7CiAgICAgICAgICAgICAgICAiZ2Vvc2l0ZSI6IFsKICAgICAgICAgICAgICAgICAgICAiY2F0ZWdvcnktYWRzLWFsbCIKICAgICAgICAgICAgICAgIF0sCiAgICAgICAgICAgICAgICAic2VydmVyIjogImRuc19ibG9jayIsCiAgICAgICAgICAgICAgICAiZGlzYWJsZV9jYWNoZSI6IHRydWUKICAgICAgICAgICAgfSwKICAgICAgICAgICAgewogICAgICAgICAgICAgICAgInF1ZXJ5X3R5cGUiOiBbCiAgICAgICAgICAgICAgICAgICAgIkEiLAogICAgICAgICAgICAgICAgICAgICJBQUFBIgogICAgICAgICAgICAgICAgXSwKICAgICAgICAgICAgICAgICJzZXJ2ZXIiOiAiZG5zX2Zha2VpcCIKICAgICAgICAgICAgfQogICAgICAgIF0sCiAgICAgICAgInN0cmF0ZWd5IjogImlwdjRfb25seSIsCiAgICAgICAgImluZGVwZW5kZW50X2NhY2hlIjogdHJ1ZSwKICAgICAgICAiZmFrZWlwIjogewogICAgICAgICAgICAiZW5hYmxlZCI6IHRydWUsCiAgICAgICAgICAgICJpbmV0NF9yYW5nZSI6ICIxOTguMTguMC4wLzE1IiwKICAgICAgICAgICAgImluZXQ2X3JhbmdlIjogImZjMDA6Oi8xOCIKICAgICAgICB9CiAgICB9LAogICAgInJvdXRlIjogewogICAgICAgICJydWxlcyI6IFsKICAgICAgICAgICAgewogICAgICAgICAgICAgICAgInByb3RvY29sIjogImRucyIsCiAgICAgICAgICAgICAgICAib3V0Ym91bmQiOiAiZG5zLW91dCIKICAgICAgICAgICAgfSwKICAgICAgICAgICAgewogICAgICAgICAgICAgICAgImdlb2lwIjogInByaXZhdGUiLAogICAgICAgICAgICAgICAgIm91dGJvdW5kIjogImRpcmVjdCIKICAgICAgICAgICAgfSwKICAgICAgICAgICAgewogICAgICAgICAgICAgICAgImNsYXNoX21vZGUiOiAiRGlyZWN0IiwKICAgICAgICAgICAgICAgICJvdXRib3VuZCI6ICJkaXJlY3QiCiAgICAgICAgICAgIH0sCiAgICAgICAgICAgIHsKICAgICAgICAgICAgICAgICJjbGFzaF9tb2RlIjogIkdsb2JhbCIsCiAgICAgICAgICAgICAgICAib3V0Ym91bmQiOiAic2VsZWN0IgogICAgICAgICAgICB9CiAgICAgICAgXSwKICAgICAgICAiZmluYWwiOiAic2VsZWN0IiwKICAgICAgICAiYXV0b19kZXRlY3RfaW50ZXJmYWNlIjogdHJ1ZQogICAgfSwKICAgICJpbmJvdW5kcyI6IFsKICAgICAgICB7CiAgICAgICAgICAgICJ0eXBlIjogInR1biIsCiAgICAgICAgICAgICJ0YWciOiAidHVuLWluIiwKICAgICAgICAgICAgImluZXQ0X2FkZHJlc3MiOiAiMTcyLjE5LjAuMS8zMCIsCiAgICAgICAgICAgICJpbmV0Nl9hZGRyZXNzIjogImZkZmU6ZGNiYTo5ODc2OjoxLzEyNiIsCiAgICAgICAgICAgICJhdXRvX3JvdXRlIjogdHJ1ZSwKICAgICAgICAgICAgInN0cmljdF9yb3V0ZSI6IHRydWUsCiAgICAgICAgICAgICJzdGFjayI6ICJtaXhlZCIsCiAgICAgICAgICAgICJzbmlmZiI6IHRydWUsCiAgICAgICAgICAgICJzbmlmZl9vdmVycmlkZV9kZXN0aW5hdGlvbiI6IGZhbHNlCiAgICAgICAgfQogICAgXSwKICAgICJvdXRib3VuZHMiOiBbCiAgICAgICAgewogICAgICAgICAgICAidHlwZSI6ICJzaGFkb3dzb2NrcyIsCiAgICAgICAgICAgICJ0YWciOiAic3MtdnBzIiwKICAgICAgICAgICAgInNlcnZlciI6ICI0NS4xMzcuMTUwLjIxNCIsCiAgICAgICAgICAgICJzZXJ2ZXJfcG9ydCI6IDY2NjYsCiAgICAgICAgICAgICJtZXRob2QiOiAiY2hhY2hhMjAtaWV0Zi1wb2x5MTMwNSIsCiAgICAgICAgICAgICJwYXNzd29yZCI6ICJMVFJ4V3R5emdlV2tLZC8vUWNUZVlBPT0iLAogICAgICAgICAgICAibXVsdGlwbGV4IjogewogICAgICAgICAgICAgICAgImVuYWJsZWQiOiB0cnVlLAogICAgICAgICAgICAgICAgInByb3RvY29sIjogImgybXV4IiwKICAgICAgICAgICAgICAgICJtYXhfY29ubmVjdGlvbnMiOiAxLAogICAgICAgICAgICAgICAgIm1pbl9zdHJlYW1zIjogNCwKICAgICAgICAgICAgICAgICJwYWRkaW5nIjogZmFsc2UKICAgICAgICAgICAgfQogICAgICAgIH0sCiAgICAgICAgewogICAgICAgICAgICAidHlwZSI6ICJzZWxlY3RvciIsCiAgICAgICAgICAgICJ0YWciOiAic2VsZWN0IiwKICAgICAgICAgICAgIm91dGJvdW5kcyI6IFsKICAgICAgICAgICAgICAgICJzcy12cHMiCiAgICAgICAgICAgIF0sCiAgICAgICAgICAgICJkZWZhdWx0IjogInNzLXZwcyIsCiAgICAgICAgICAgICJpbnRlcnJ1cHRfZXhpc3RfY29ubmVjdGlvbnMiOiBmYWxzZQogICAgICAgIH0sCiAgICAgICAgewogICAgICAgICAgICAidHlwZSI6ICJkaXJlY3QiLAogICAgICAgICAgICAidGFnIjogImRpcmVjdCIKICAgICAgICB9LAogICAgICAgIHsKICAgICAgICAgICAgInR5cGUiOiAiYmxvY2siLAogICAgICAgICAgICAidGFnIjogImJsb2NrIgogICAgICAgIH0sCiAgICAgICAgewogICAgICAgICAgICAidHlwZSI6ICJkbnMiLAogICAgICAgICAgICAidGFnIjogImRucy1vdXQiCiAgICAgICAgfQogICAgXSwKICAgICJleHBlcmltZW50YWwiOiB7CiAgICAgICAgImNhY2hlX2ZpbGUiOiB7CiAgICAgICAgICAgICJlbmFibGVkIjogdHJ1ZQogICAgICAgIH0KICAgIH0sCiAgICAibnRwIjogewogICAgICAgICJlbmFibGVkIjogdHJ1ZSwKICAgICAgICAic2VydmVyIjogInRpbWUuYXBwbGUuY29tIiwKICAgICAgICAic2VydmVyX3BvcnQiOiAxMjMsCiAgICAgICAgImludGVydmFsIjogIjMwbSIsCiAgICAgICAgImRldG91ciI6ICJkaXJlY3QiCiAgICB9Cn0="
    val wireGuardJson = """{
    "log": {
        "level": "debug"
    },
    "dns": {
        "servers": [
            {
                "tag": "local",
                "address": "https://1.1.1.1/dns-query",
                "detour": "direct"
            }
        ],
        "reverse_mapping": true,
        "strategy": "prefer_ipv4"
    },
    "inbounds": [
        {
            "type": "tun",
            "tag": "tun-in",
            "interface_name": "ipv4-tun",
            "mtu": 1500,
            "inet4_address": "172.19.0.1/28",
            "auto_route": true,
            "strict_route": true,
            "stack": "mixed",
            "sniff": true,
            "sniff_override_destination": false
        }
    ],
    "outbounds": [
        {
            "type": "wireguard",
            "tag": "wg",
            "server": "64.225.79.80",
            "server_port": 51820,
            "system_interface": false,
            "interface_name": "wg0",
            "local_address": [
                "10.7.14.1/32"
            ],
            "private_key": "cI6Xfg8oMB2yaLzS811wOqkqOBnEoMsRdIVYsizioXE=",
            "peer_public_key": "Sg1uVt0aXkS4/4A5Ao5vpw8s7kU5j6rYHnwW5IHhcn0=",
            "pre_shared_key": "PzQcl7iqJcdS4k3KIxrgBUOe9xYs/+RU4zOZhyZxV9g=",
            "reserved": [
                0,
                0,
                0
            ],
            "mtu": 1280,
            "workers": 4
        },
        {
            "type": "dns",
            "tag": "dns"
        },
        {
            "type": "direct",
            "tag": "direct"
        },
        {
            "type": "block",
            "tag": "block"
        },
        {
            "type": "selector",
            "tag": "select",
            "outbounds": [
                "wg"
            ],
            "default": "wg",
            "interrupt_exist_connections": false
        }
    ],
    "route": {
        "rules": [
            {
                "protocol": "dns",
                "outbound": "dns"
            },
            {
                "geoip": "private",
                "outbound": "direct"
            },
            {
                "geoip": "cn",
                "geosite": "cn",
                "outbound": "wg"
            }
        ],
        "final": "wg",
        "auto_detect_interface": true
    },
    "ntp": {
        "enabled": true,
        "server": "time.apple.com",
        "server_port": 123,
        "interval": "30m0s",
        "detour": "direct"
    }
}"""
}