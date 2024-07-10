package de.blinkt

import com.phoenixLib.commoncore.model.VPNType
import com.phoenixLib.commoncore.model.VpnProfile

object VpnConfigs {
    val openVpnConf =
        "Y2xpZW50CnByb3RvIHVkcApleHBsaWNpdC1leGl0LW5vdGlmeQpyZW1vdGUgNDUuMTM3LjE1MC4yMTQgMTE5NApibG9jay1pcHY2CmRldiB0dW4KcmVzb2x2LXJldHJ5IGluZmluaXRlCm5vYmluZApwZXJzaXN0LWtleQpwZXJzaXN0LXR1bgpyZW1vdGUtY2VydC10bHMgc2VydmVyCnZlcmlmeS14NTA5LW5hbWUgc2VydmVyX2p6WW9lNnlHUExnNWlyR0YgbmFtZQphdXRoIFNIQTI1NgphdXRoLW5vY2FjaGUKYXV0aC11c2VyLXBhc3MKI2RoY3Atb3B0aW9uIEROUyA4LjguOC44CiNkaGNwLW9wdGlvbiBET01BSU4gZ29vZ2xlLmNvbQpjaXBoZXIgIEFFUy0xMjgtR0NNCnRscy1jbGllbnQKdGxzLXZlcnNpb24tbWluIDEuMgp0bHMtY2lwaGVyIFRMUy1FQ0RIRS1FQ0RTQS1XSVRILUFFUy0xMjgtR0NNLVNIQTI1NgppZ25vcmUtdW5rbm93bi1vcHRpb24gYmxvY2stb3V0c2lkZS1kbnMKc2V0ZW52IG9wdCBibG9jay1vdXRzaWRlLWRucyAjIFByZXZlbnQgV2luZG93cyAxMCBETlMgbGVhawpzZXRlbnYgQ0xJRU5UX0NFUlQgMAp2ZXJiIDMKPGNhPgotLS0tLUJFR0lOIENFUlRJRklDQVRFLS0tLS0KTUlJQjJEQ0NBWDJnQXdJQkFnSVVDYVNZZ3J3QUVlZ2gzSnVLMmR5Q0xQR2RkSHN3Q2dZSUtvWkl6ajBFQXdJdwpIakVjTUJvR0ExVUVBd3dUWTI1ZmREVXlVRVF3TTBaUVpEVmFlVmg0UkRBZUZ3MHlOREEwTWpJeE1ESTRNRGxhCkZ3MHpOREEwTWpBeE1ESTRNRGxhTUI0eEhEQWFCZ05WQkFNTUUyTnVYM1ExTWxCRU1ETkdVR1ExV25sWWVFUXcKV1RBVEJnY3Foa2pPUFFJQkJnZ3Foa2pPUFFNQkJ3TkNBQVF5S1BRR214bWlzczB6cUt1TU1hcWNiS0d2dGdscQpFelgzSHlMbitBeHJRajh3anp0S1JORERHNkJKbCtLQi85alc1TFM1MFV5WEZYeThlLzZuVVU1Q280R1lNSUdWCk1Bd0dBMVVkRXdRRk1BTUJBZjh3SFFZRFZSME9CQllFRkpvaFVDNzhHUFR4UGs2Q1NlR3JGM0ZlSzBoc01Ga0cKQTFVZEl3UlNNRkNBRkpvaFVDNzhHUFR4UGs2Q1NlR3JGM0ZlSzBoc29TS2tJREFlTVJ3d0dnWURWUVFEREJOagpibDkwTlRKUVJEQXpSbEJrTlZwNVdIaEVnaFFKcEppQ3ZBQVI2Q0hjbTRyWjNJSXM4WjEwZXpBTEJnTlZIUThFCkJBTUNBUVl3Q2dZSUtvWkl6ajBFQXdJRFNRQXdSZ0loQUtEQmlSaHIwZ2lFeVpyWnRTYXRvenJGbi94Yld5YWIKcFRBVVNnWFRIaGFRQWlFQXlIZllZcmNNR1R0cEFSYklacFYyWFAzZ3BxR3ZOS0hVbHZFMXJtdVNIYUk9Ci0tLS0tRU5EIENFUlRJRklDQVRFLS0tLS0KPC9jYT4KPHRscy1jcnlwdD4KIwojIDIwNDggYml0IE9wZW5WUE4gc3RhdGljIGtleQojCi0tLS0tQkVHSU4gT3BlblZQTiBTdGF0aWMga2V5IFYxLS0tLS0KYzE3YWVjOTExOGM0ZjAwZGIwZTE1Y2NiMGNlODMyNmMKYTc0OTk3MWM1OTg2ZjExYzNiOTAwMzcxZGJkMGU5MGQKOWEyNGEwZmU5YWYyMGY1YmNjOGM0MTJmYjFjNGJmMTMKODE4YTkxMWViNzNiZWJjYzU5NjBjYTA2NjVkNjkyMjgKNDk0NzRmMTk1ZWI4MWM4NmE1YTNhODI0MDA4NWJkMzIKNmI4ZGE3MWY1ODY2MDkwNGZjMjM1NTRkMTEwMmQzMzMKZjFlMzQzMGIxYTU2N2QwYTViZjUyMTI3NjQ0ZjBmZTgKZTI0Y2U2Yzg0MzE1MDJjZGRmZmU4MjQ2ZjkwNWQ4ZTQKODcxOTM2OTcwMTUwMTYzODlmNzgzYWU5NzRmNTA5MTEKZTc1ZThmMThiNjNjM2M3ZGZhY2M2YjA0MmYxNTA2ZWYKNWM3N2E5NzQzZmU3ZTQ4ZWIzOWY5NjgxNWVkNjgxNTYKYzAxZGE4N2ZhY2M2NDc1OWQzMTUxZmJhNzIxNmM3ZjMKZGNmZjExODNjOTdmNGMyMzJhYmQyOTdiODFlMjQyNDgKZWYzYTkwOTE2ZmRiMmY4NTQ3MTI0OGIwZjY0ODQzNmMKY2UzMGM1Zjk1NDc1ZjFjN2U4MDYzYTkwNTZmYWE3ZmEKMmQ3ZWRhMmQwODE3Yjg5ZmE5ZTI2ZmU4OGJlZjQ0MzEKLS0tLS1FTkQgT3BlblZQTiBTdGF0aWMga2V5IFYxLS0tLS0KPC90bHMtY3J5cHQ+Cg=="
    val openVpnIP = "45.137.150.214:1194"
    val wgTunnelConfig =
        "[Interface]\nAddress = 10.7.19.4/32\nDNS = 8.8.8.8, 8.8.4.4\nPrivateKey = OK8m04WBsQvuq0Tb3zj7ZAxLkeVTrprZedHaUrTdRFU=\n\n[Peer]\nPublicKey = bdOcBvDqwdykOi5A1fC2VnROJNhv3+iw0dTkgx5jPQk=\nPresharedKey = cMy4mUafmwJ4+Z5LGuHGFzAYY0FYrMUyHOjEu4/k0pI=\nAllowedIPs = 0.0.0.0/0, ::/0\nEndpoint = 134.122.54.172:51820\nPersistentKeepalive = 25"
    val singBoxConfigEnc =
        "ewogICAgImxvZyI6IHsKICAgICAgICAiZGlzYWJsZWQiOiBmYWxzZSwKICAgICAgICAibGV2ZWwiOiAid2FybiIsCiAgICAgICAgInRpbWVzdGFtcCI6IHRydWUKICAgIH0sCiAgICAiZG5zIjogewogICAgICAgICJzZXJ2ZXJzIjogWwogICAgICAgICAgICB7CiAgICAgICAgICAgICAgICAidGFnIjogImRuc19ibG9jayIsCiAgICAgICAgICAgICAgICAiYWRkcmVzcyI6ICJyY29kZTovL3N1Y2Nlc3MiCiAgICAgICAgICAgIH0sCiAgICAgICAgICAgIHsKICAgICAgICAgICAgICAgICJ0YWciOiAiZG5zX2Zha2VpcCIsCiAgICAgICAgICAgICAgICAiYWRkcmVzcyI6ICJmYWtlaXAiCiAgICAgICAgICAgIH0sCiAgICAgICAgICAgIHsKICAgICAgICAgICAgICAgICJ0YWciOiAiZG5zX2xvY2FsIiwKICAgICAgICAgICAgICAgICJhZGRyZXNzIjogIjIyMy41LjUuNSIsCiAgICAgICAgICAgICAgICAiZGV0b3VyIjogImRpcmVjdCIKICAgICAgICAgICAgfQogICAgICAgIF0sCiAgICAgICAgInJ1bGVzIjogWwogICAgICAgICAgICB7CiAgICAgICAgICAgICAgICAib3V0Ym91bmQiOiAiYW55IiwKICAgICAgICAgICAgICAgICJzZXJ2ZXIiOiAiZG5zX2xvY2FsIgogICAgICAgICAgICB9LAogICAgICAgICAgICB7CiAgICAgICAgICAgICAgICAiZ2Vvc2l0ZSI6IFsKICAgICAgICAgICAgICAgICAgICAiY2F0ZWdvcnktYWRzLWFsbCIKICAgICAgICAgICAgICAgIF0sCiAgICAgICAgICAgICAgICAic2VydmVyIjogImRuc19ibG9jayIsCiAgICAgICAgICAgICAgICAiZGlzYWJsZV9jYWNoZSI6IHRydWUKICAgICAgICAgICAgfSwKICAgICAgICAgICAgewogICAgICAgICAgICAgICAgInF1ZXJ5X3R5cGUiOiBbCiAgICAgICAgICAgICAgICAgICAgIkEiLAogICAgICAgICAgICAgICAgICAgICJBQUFBIgogICAgICAgICAgICAgICAgXSwKICAgICAgICAgICAgICAgICJzZXJ2ZXIiOiAiZG5zX2Zha2VpcCIKICAgICAgICAgICAgfQogICAgICAgIF0sCiAgICAgICAgInN0cmF0ZWd5IjogImlwdjRfb25seSIsCiAgICAgICAgImluZGVwZW5kZW50X2NhY2hlIjogdHJ1ZSwKICAgICAgICAiZmFrZWlwIjogewogICAgICAgICAgICAiZW5hYmxlZCI6IHRydWUsCiAgICAgICAgICAgICJpbmV0NF9yYW5nZSI6ICIxOTguMTguMC4wLzE1IiwKICAgICAgICAgICAgImluZXQ2X3JhbmdlIjogImZjMDA6Oi8xOCIKICAgICAgICB9CiAgICB9LAogICAgInJvdXRlIjogewogICAgICAgICJydWxlcyI6IFsKICAgICAgICAgICAgewogICAgICAgICAgICAgICAgInByb3RvY29sIjogImRucyIsCiAgICAgICAgICAgICAgICAib3V0Ym91bmQiOiAiZG5zLW91dCIKICAgICAgICAgICAgfSwKICAgICAgICAgICAgewogICAgICAgICAgICAgICAgImdlb2lwIjogInByaXZhdGUiLAogICAgICAgICAgICAgICAgIm91dGJvdW5kIjogImRpcmVjdCIKICAgICAgICAgICAgfSwKICAgICAgICAgICAgewogICAgICAgICAgICAgICAgImNsYXNoX21vZGUiOiAiRGlyZWN0IiwKICAgICAgICAgICAgICAgICJvdXRib3VuZCI6ICJkaXJlY3QiCiAgICAgICAgICAgIH0sCiAgICAgICAgICAgIHsKICAgICAgICAgICAgICAgICJjbGFzaF9tb2RlIjogIkdsb2JhbCIsCiAgICAgICAgICAgICAgICAib3V0Ym91bmQiOiAic2VsZWN0IgogICAgICAgICAgICB9CiAgICAgICAgXSwKICAgICAgICAiZmluYWwiOiAic2VsZWN0IiwKICAgICAgICAiYXV0b19kZXRlY3RfaW50ZXJmYWNlIjogdHJ1ZQogICAgfSwKICAgICJpbmJvdW5kcyI6IFsKICAgICAgICB7CiAgICAgICAgICAgICJ0eXBlIjogInR1biIsCiAgICAgICAgICAgICJ0YWciOiAidHVuLWluIiwKICAgICAgICAgICAgImluZXQ0X2FkZHJlc3MiOiAiMTcyLjE5LjAuMS8zMCIsCiAgICAgICAgICAgICJpbmV0Nl9hZGRyZXNzIjogImZkZmU6ZGNiYTo5ODc2OjoxLzEyNiIsCiAgICAgICAgICAgICJhdXRvX3JvdXRlIjogdHJ1ZSwKICAgICAgICAgICAgInN0cmljdF9yb3V0ZSI6IHRydWUsCiAgICAgICAgICAgICJzdGFjayI6ICJtaXhlZCIsCiAgICAgICAgICAgICJzbmlmZiI6IHRydWUsCiAgICAgICAgICAgICJzbmlmZl9vdmVycmlkZV9kZXN0aW5hdGlvbiI6IGZhbHNlCiAgICAgICAgfQogICAgXSwKICAgICJvdXRib3VuZHMiOiBbCiAgICAgICAgewogICAgICAgICAgICAidHlwZSI6ICJzaGFkb3dzb2NrcyIsCiAgICAgICAgICAgICJ0YWciOiAic3MtdnBzIiwKICAgICAgICAgICAgInNlcnZlciI6ICI0NS4xMzcuMTUwLjIxNCIsCiAgICAgICAgICAgICJzZXJ2ZXJfcG9ydCI6IDY2NjYsCiAgICAgICAgICAgICJtZXRob2QiOiAiY2hhY2hhMjAtaWV0Zi1wb2x5MTMwNSIsCiAgICAgICAgICAgICJwYXNzd29yZCI6ICJMVFJ4V3R5emdlV2tLZC8vUWNUZVlBPT0iLAogICAgICAgICAgICAibXVsdGlwbGV4IjogewogICAgICAgICAgICAgICAgImVuYWJsZWQiOiB0cnVlLAogICAgICAgICAgICAgICAgInByb3RvY29sIjogImgybXV4IiwKICAgICAgICAgICAgICAgICJtYXhfY29ubmVjdGlvbnMiOiAxLAogICAgICAgICAgICAgICAgIm1pbl9zdHJlYW1zIjogNCwKICAgICAgICAgICAgICAgICJwYWRkaW5nIjogZmFsc2UKICAgICAgICAgICAgfQogICAgICAgIH0sCiAgICAgICAgewogICAgICAgICAgICAidHlwZSI6ICJzZWxlY3RvciIsCiAgICAgICAgICAgICJ0YWciOiAic2VsZWN0IiwKICAgICAgICAgICAgIm91dGJvdW5kcyI6IFsKICAgICAgICAgICAgICAgICJzcy12cHMiCiAgICAgICAgICAgIF0sCiAgICAgICAgICAgICJkZWZhdWx0IjogInNzLXZwcyIsCiAgICAgICAgICAgICJpbnRlcnJ1cHRfZXhpc3RfY29ubmVjdGlvbnMiOiBmYWxzZQogICAgICAgIH0sCiAgICAgICAgewogICAgICAgICAgICAidHlwZSI6ICJkaXJlY3QiLAogICAgICAgICAgICAidGFnIjogImRpcmVjdCIKICAgICAgICB9LAogICAgICAgIHsKICAgICAgICAgICAgInR5cGUiOiAiYmxvY2siLAogICAgICAgICAgICAidGFnIjogImJsb2NrIgogICAgICAgIH0sCiAgICAgICAgewogICAgICAgICAgICAidHlwZSI6ICJkbnMiLAogICAgICAgICAgICAidGFnIjogImRucy1vdXQiCiAgICAgICAgfQogICAgXSwKICAgICJleHBlcmltZW50YWwiOiB7CiAgICAgICAgImNhY2hlX2ZpbGUiOiB7CiAgICAgICAgICAgICJlbmFibGVkIjogdHJ1ZQogICAgICAgIH0KICAgIH0sCiAgICAibnRwIjogewogICAgICAgICJlbmFibGVkIjogdHJ1ZSwKICAgICAgICAic2VydmVyIjogInRpbWUuYXBwbGUuY29tIiwKICAgICAgICAic2VydmVyX3BvcnQiOiAxMjMsCiAgICAgICAgImludGVydmFsIjogIjMwbSIsCiAgICAgICAgImRldG91ciI6ICJkaXJlY3QiCiAgICB9Cn0="
    val wgTunConf2 =
        "[Interface]\n" + "Address = 192.168.1.200/24\n" + "PrivateKey = oD1vB2Esia0akOrnwxCxgB7jOwKs83QFmD6kApbm+1I=\n" + "DNS = 8.8.8.8\n" + "\n" + "[Peer]\n" + "PublicKey = 4YVOLdZ9aCxI/Gp/P1oszjrVSqDl+WVUoaugfIwZMlw=\n" + "Endpoint = 51.68.220.168:51820\n" + "AllowedIPs = 0.0.0.0/0\n" + "PersistentKeepalive = 25\n"

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

    fun getVPNProfile(vpnType: VPNType) = when (vpnType) {
        VPNType.OPENVPN -> {
            VpnProfile(
                vpnType = VPNType.OPENVPN,
                userName = "test1",
                password = "1111112",
                vpnConfig = openVpnConf,
                serverIP = openVpnIP
            )
        }

        VPNType.SINGBOX -> {
            VpnProfile(
                vpnType = VPNType.SINGBOX,
                userName = "ss",
                password = "123456",
                vpnConfig = wireGuardJson,
                serverIP = openVpnIP
            )
        }

        VPNType.WIREGUARD -> {
            VpnProfile(
                vpnType = VPNType.WIREGUARD,
                userName = "ss",
                password = "123456",
                vpnConfig = wgTunnelConfig,
                serverIP = openVpnIP
            )
        }

        else -> {
            VpnProfile(
                vpnType = VPNType.OPENVPN,
                userName = "test1",
                password = "1111112",
                vpnConfig = openVpnConf,
                serverIP = openVpnIP
            )
        }
    }

}