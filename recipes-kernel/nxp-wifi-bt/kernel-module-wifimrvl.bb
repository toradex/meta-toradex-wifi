SUMMARY = "Kernel loadable module for NXP WiFi chip"
LICENSE = "CLOSED"

inherit module

KERNEL_MODULE_WIFI_INTERFACE:sd-sd = "sdxxx"
KERNEL_MODULE_WIFI_INTERFACE:sd-uart = "moal"
KERNEL_MODULE_WIFI_INTERFACE:pcie-usb = "pciexxx"

RPROVIDES:${PN}:append = " kernel-module-${KERNEL_MODULE_WIFI_INTERFACE} kernel-module-mlan "

KERNEL_MODULE_PROBECONF:append = " ${KERNEL_MODULE_WIFI_INTERFACE} "

module_conf_${KERNEL_MODULE_WIFI_INTERFACE} = "options ${KERNEL_MODULE_WIFI_INTERFACE} cal_data_cfg=nxp/cal_data.conf cfg80211_wext=0xc fw_name=nxp/${FIRMWARE_BIN}"

module_conf_${KERNEL_MODULE_WIFI_INTERFACE}:mfg-mode:mfgmode-fw = "options ${KERNEL_MODULE_WIFI_INTERFACE} cal_data_cfg=none cfg80211_wext=0xf mfg_mode=1 fw_name=nxp/${FIRMWARE_BIN_MFGMODE}"

module_conf_${KERNEL_MODULE_WIFI_INTERFACE}:mfg-mode = "options ${KERNEL_MODULE_WIFI_INTERFACE} cal_data_cfg=none cfg80211_wext=0xf fw_name=nxp/${FIRMWARE_BIN_MFGMODE}"

SRC_URI = "\
    ${NXP_DOWNSTREAM_DRIVER_PKG_FILENAME} \
"
SRC_URI[sha256sum] = "${NXP_DOWNSTREAM_DRIVER_PKG_SHA1}"

SRC_URI:append = "\
    file://cal_data.conf \
"

SRC_URI:append:sd-sd = "\
   file://sd-sd/0001-Adapt-Makefile-for-Yocto-build.patch \
   file://sd-sd/0001-Changes-to-support-kernel-6.6.0.patch \
"

SRC_URI:append:sd-uart = "\
   file://sd-uart/0001-Adapt-Makefile-for-Yocto-build.patch \
   file://sd-uart/Fix-sa_data-structure-fields.patch \
"

SRC_URI:append:pcie-usb = "\
   file://pcie-usb/0001-Adapt-Makefile-for-Yocto-build.patch \
"

S:pcie-usb = "${WORKDIR}/src/pcie-usb/wlan_src"
S:sd-sd = "${WORKDIR}/src/sd-sd/wlan_src"
S:sd-uart = "${WORKDIR}/src/sd-uart/wlan_src"

DEPENDS += "bc-native"
RDEPENDS_${PN} += "toradex-wifi-config"

do_install:append() {
    install -d ${D}${base_libdir}/firmware/nxp
    install -m 0644 ${WORKDIR}/cal_data.conf ${D}${base_libdir}/firmware/nxp
}

FILES:${PN} += "${base_libdir}/firmware/nxp"

COMPATIBLE_MACHINE = "(colibri-imx6ull|colibri-imx8x|verdin-imx8mm|verdin-imx8mp|verdin-am62|apalis-imx8)"

