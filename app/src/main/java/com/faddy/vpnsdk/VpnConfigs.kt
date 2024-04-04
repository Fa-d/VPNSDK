package com.faddy.vpnsdk

object VpnConfigs {
    val openVpnConf =
        "Y2xpZW50CnByb3RvIHVkcApleHBsaWNpdC1leGl0LW5vdGlmeQpyZW1vdGUgMTAuMC4wLjEgMTE5NApkZXYgdHVuCnJlc29sdi1yZXRyeSBpbmZpbml0ZQpub2JpbmQKcGVyc2lzdC1rZXkKcGVyc2lzdC10dW4KcmVtb3RlLWNlcnQtdGxzIHNlcnZlcgp2ZXJpZnkteDUwOS1uYW1lIHNlcnZlcl9FWEFNUExFX1ZFUklGWV9OQU1FIG5hbWUKYXV0aCBTSEEyNTYKYXV0aC1ub2NhY2hlCmF1dGgtdXNlci1wYXNzCmRoY3Atb3B0aW9uIEROUyA4LjguOC44CmRoY3Atb3B0aW9uIERPTUFJTiBnb29nbGUuY29tCmNpcGhlciAgQUVTLTEyOC1HQ00KdGxzLWNsaWVudAp0bHMtdmVyc2lvbi1taW4gMS4yCnRscy1jaXBoZXIgVExTLUVDREhFLUVDRFNBLVdJVEgtQUVTLTEyOC1HQ00tU0hBMjU2Cmlnbm9yZS11bmtub3duLW9wdGlvbiBibG9jay1vdXRzaWRlLWRucwpzZXRlbnYgb3B0IGJsb2NrLW91dHNpZGUtZG5zICMgUHJldmVudCBXaW5kb3dzIDEwIEROUyBsZWFrCnNldGVudiBDTElFTlRfQ0VSVCAwCnZlcmIgMwo8Y2E+Ci0tLS0tQkVHSU4gQ0VSVElGSUNBVEUtLS0tLQpZT1VSX0NBX0NFUlRJRklDQVRFX0hFUkUKLS0tLS1FTkQgQ0VSVElGSUNBVEUtLS0tLQo8L2NhPgo8dGxzLWNyeXB0PgojCiMgMjA0OCBiaXQgT3BlblZQTiBzdGF0aWMga2V5CiMKLS0tLS1CRUdJTiBPcGVuVlBOIFN0YXRpYyBrZXkgVjEtLS0tLQpZT1VSX09QRU5WUE5fU1RBVElDX0tFWV9IRVJFCi0tLS0tRU5EIE9wZW5WUE4gU3RhdGljIGtleSBWMS0tLS0tCjwvdGxzLWNyeXB0Pgo="
    val openVpnIP = "10.0.0.1:1194"
    val wgTunnelConfig =
        "[Interface]\nAddress = 10.7.19.4/32\nDNS = 8.8.8.8, 8.8.4.4\nPrivateKey = YOUR_PRIVATE_KEY_HERE\n\n[Peer]\nPublicKey = YOUR_PUBLIC_KEY_HERE\nPresharedKey = YOUR_PRESHARED_KEY_HERE\nAllowedIPs = 0.0.0.0/0, ::/0\nEndpoint = 10.0.0.2:51820\nPersistentKeepalive = 25"
    val singBoxConfigEnc =
        "ewogICAgImxvZyI6IHsKICAgICAgICAiZGlzYWJsZWQiOiBmYWxzZSwKICAgICAgICAibGV2ZWwiOiAid2FybiIsCiAgICAgICAgInRpbWVzdGFtcCI6IHRydWUKICAgIH0sCiAgICAiZG5zIjogewogICAgICAgICJzZXJ2ZXJzIjogWwogICAgICAgICAgICB7CiAgICAgICAgICAgICAgICAidGFnIjogImRuc19ibG9jayIsCiAgICAgICAgICAgICAgICAiYWRkcmVzcyI6ICJyY29kZTovL3N1Y2Nlc3MiCiAgICAgICAgICAgIH0sCiAgICAgICAgICAgIHsKICAgICAgICAgICAgICAgICJ0YWciOiAiZG5zX2Zha2VpcCIsCiAgICAgICAgICAgICAgICAiYWRkcmVzcyI6ICJmYWtlaXAiCiAgICAgICAgICAgIH0sCiAgICAgICAgICAgIHsKICAgICAgICAgICAgICAgICJ0YWciOiAiZG5zX2xvY2FsIiwKICAgICAgICAgICAgICAgICJhZGRyZXNzIjogIjIyMy41LjUuNSIsCiAgICAgICAgICAgICAgICAiZGV0b3VyIjogImRpcmVjdCIKICAgICAgICAgICAgfQogICAgICAgIF0sCiAgICAgICAgInJ1bGVzIjogWwogICAgICAgICAgICB7CiAgICAgICAgICAgICAgICAib3V0Ym91bmQiOiAiYW55IiwKICAgICAgICAgICAgICAgICJzZXJ2ZXIiOiAiZG5zX2xvY2FsIgogICAgICAgICAgICB9LAogICAgICAgICAgICB7CiAgICAgICAgICAgICAgICAiZ2Vvc2l0ZSI6IFsKICAgICAgICAgICAgICAgICAgICAiY2F0ZWdvcnktYWRzLWFsbCIKICAgICAgICAgICAgICAgIF0sCiAgICAgICAgICAgICAgICAic2VydmVyIjogImRuc19ibG9jayIsCiAgICAgICAgICAgICAgICAiZGlzYWJsZV9jYWNoZSI6IHRydWUKICAgICAgICAgICAgfSwKICAgICAgICAgICAgewogICAgICAgICAgICAgICAgInF1ZXJ5X3R5cGUiOiBbCiAgICAgICAgICAgICAgICAgICAgIkEiLAogICAgICAgICAgICAgICAgICAgICJBQUFBIgogICAgICAgICAgICAgICAgXSwKICAgICAgICAgICAgICAgICJzZXJ2ZXIiOiAiZG5zX2Zha2VpcCIKICAgICAgICAgICAgfQogICAgICAgIF0sCiAgICAgICAgInN0cmF0ZWd5IjogImlwdjRfb25seSIsCiAgICAgICAgImluZGVwZW5kZW50X2NhY2hlIjogdHJ1ZSwKICAgICAgICAiZmFrZWlwIjogewogICAgICAgICAgICAiZW5hYmxlZCI6IHRydWUsCiAgICAgICAgICAgICJpbmV0NF9yYW5nZSI6ICIxOTguMTguMC4wLzE1IiwKICAgICAgICAgICAgImluZXQ2X3JhbmdlIjogImZjMDA6Oi8xOCIKICAgICAgICB9CiAgICB9LAogICAgInJvdXRlIjogewogICAgICAgICJydWxlcyI6IFsKICAgICAgICAgICAgewogICAgICAgICAgICAgICAgInByb3RvY29sIjogImRucyIsCiAgICAgICAgICAgICAgICAib3V0Ym91bmQiOiAiZG5zLW91dCIKICAgICAgICAgICAgfSwKICAgICAgICAgICAgewogICAgICAgICAgICAgICAgImdlb2lwIjogInByaXZhdGUiLAogICAgICAgICAgICAgICAgIm91dGJvdW5kIjogImRpcmVjdCIKICAgICAgICAgICAgfSwKICAgICAgICAgICAgewogICAgICAgICAgICAgICAgImNsYXNoX21vZGUiOiAiRGlyZWN0IiwKICAgICAgICAgICAgICAgICJvdXRib3VuZCI6ICJkaXJlY3QiCiAgICAgICAgICAgIH0sCiAgICAgICAgICAgIHsKICAgICAgICAgICAgICAgICJjbGFzaF9tb2RlIjogIkdsb2JhbCIsCiAgICAgICAgICAgICAgICAib3V0Ym91bmQiOiAic2VsZWN0IgogICAgICAgICAgICB9CiAgICAgICAgXSwKICAgICAgICAiZmluYWwiOiAic2VsZWN0IiwKICAgICAgICAiYXV0b19kZXRlY3RfaW50ZXJmYWNlIjogdHJ1ZQogICAgfSwKICAgICJpbmJvdW5kcyI6IFsKICAgICAgICB7CiAgICAgICAgICAgICJ0eXBlIjogInR1biIsCiAgICAgICAgICAgICJ0YWciOiAidHVuLWluIiwKICAgICAgICAgICAgImluZXQ0X2FkZHJlc3MiOiAiMTcyLjE5LjAuMS8zMCIsCiAgICAgICAgICAgICJpbmV0Nl9hZGRyZXNzIjogImZkZmU6ZGNiYTo5ODc2OjoxLzEyNiIsCiAgICAgICAgICAgICJhdXRvX3JvdXRlIjogdHJ1ZSwKICAgICAgICAgICAgInN0cmljdF9yb3V0ZSI6IHRydWUsCiAgICAgICAgICAgICJzdGFjayI6ICJtaXhlZCIsCiAgICAgICAgICAgICJzbmlmZiI6IHRydWUsCiAgICAgICAgICAgICJzbmlmZl9vdmVycmlkZV9kZXN0aW5hdGlvbiI6IGZhbHNlCiAgICAgICAgfQogICAgXSwKICAgICJvdXRib3VuZHMiOiBbCiAgICAgICAgewogICAgICAgICAgICAidHlwZSI6ICJzaGFkb3dzb2NrcyIsCiAgICAgICAgICAgICJ0YWciOiAic3MtdnBzIiwKICAgICAgICAgICAgInNlcnZlciI6ICIxMC4wLjAuNCIsCiAgICAgICAgICAgICJzZXJ2ZXJfcG9ydCI6IDY2NjYsCiAgICAgICAgICAgICJtZXRob2QiOiAiY2hhY2hhMjAtaWV0Zi1wb2x5MTMwNSIsCiAgICAgICAgICAgICJwYXNzd29yZCI6ICJZT1VSX1BBU1NXT1JEX0hFUkUiLAogICAgICAgICAgICAibXVsdGlwbGV4IjogewogICAgICAgICAgICAgICAgImVuYWJsZWQiOiB0cnVlLAogICAgICAgICAgICAgICAgInByb3RvY29sIjogImgybXV4IiwKICAgICAgICAgICAgICAgICJtYXhfY29ubmVjdGlvbnMiOiAxLAogICAgICAgICAgICAgICAgIm1pbl9zdHJlYW1zIjogNCwKICAgICAgICAgICAgICAgICJwYWRkaW5nIjogZmFsc2UKICAgICAgICAgICAgfQogICAgICAgIH0sCiAgICAgICAgewogICAgICAgICAgICAidHlwZSI6ICJzZWxlY3RvciIsCiAgICAgICAgICAgICJ0YWciOiAic2VsZWN0IiwKICAgICAgICAgICAgIm91dGJvdW5kcyI6IFsKICAgICAgICAgICAgICAgICJzcy12cHMiCiAgICAgICAgICAgIF0sCiAgICAgICAgICAgICJkZWZhdWx0IjogInNzLXZwcyIsCiAgICAgICAgICAgICJpbnRlcnJ1cHRfZXhpc3RfY29ubmVjdGlvbnMiOiBmYWxzZQogICAgICAgIH0sCiAgICAgICAgewogICAgICAgICAgICAidHlwZSI6ICJkaXJlY3QiLAogICAgICAgICAgICAidGFnIjogImRpcmVjdCIKICAgICAgICB9LAogICAgICAgIHsKICAgICAgICAgICAgInR5cGUiOiAiYmxvY2siLAogICAgICAgICAgICAidGFnIjogImJsb2NrIgogICAgICAgIH0sCiAgICAgICAgewogICAgICAgICAgICAidHlwZSI6ICJkbnMiLAogICAgICAgICAgICAidGFnIjogImRucy1vdXQiCiAgICAgICAgfQogICAgXSwKICAgICJleHBlcmltZW50YWwiOiB7CiAgICAgICAgImNhY2hlX2ZpbGUiOiB7CiAgICAgICAgICAgICJlbmFibGVkIjogdHJ1ZQogICAgICAgIH0KICAgIH0sCiAgICAibnRwIjogewogICAgICAgICJlbmFibGVkIjogdHJ1ZSwKICAgICAgICAic2VydmVyIjogInRpbWUuYXBwbGUuY29tIiwKICAgICAgICAic2VydmVyX3BvcnQiOiAxMjMsCiAgICAgICAgImludGVydmFsIjogIjMwbSIsCiAgICAgICAgImRldG91ciI6ICJkaXJlY3QiCiAgICB9Cn0="
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
            "server": "10.0.0.5",
            "server_port": 51820,
            "system_interface": false,
            "interface_name": "wg0",
            "local_address": [
                "10.7.14.1/32"
            ],
            "private_key": "YOUR_PRIVATE_KEY_HERE",
            "peer_public_key": "YOUR_PUBLIC_KEY_HERE",
            "pre_shared_key": "YOUR_PRESHARED_KEY_HERE",
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