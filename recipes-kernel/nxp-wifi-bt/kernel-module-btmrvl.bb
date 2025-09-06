SUMMARY = "Kernel loadable module for NXP Bluetooth chip"
LICENSE = "CLOSED"

inherit module

DEPENDS="kernel-module-wifimrvl"
RPROVIDES:${PN}:append = " kernel-module-btxxx "

module_conf_btxxx = "options btxxx cal_data_cfg=nxp/cal_data.conf cfg80211_wext=0xc fw_name=nxp/${FIRMWARE_BIN}"

module_conf_btxxx:mfg-mode:mfgmode-fw = "options btxxx cal_data_cfg=none cfg80211_wext=0xf mfg_mode=1 fw_name=nxp/${FIRMWARE_BIN_MFGMODE}"

module_conf_btxxx:mfg-mode = "options btxxx cal_data_cfg=none cfg80211_wext=0xf fw_name=nxp/${FIRMWARE_BIN_MFGMODE}"

KERNEL_MODULE_PROBECONF:append = " btxxx "

SRC_URI = "\
    ${NXP_DOWNSTREAM_DRIVER_PKG_FILENAME} \
"
SRC_URI[sha256sum] = "${NXP_DOWNSTREAM_DRIVER_PKG_SHA1}"

SRC_URI:append:pcie-usb = "\
    file://pcie-usb/0001-Adapt-Makefile-for-Yocto-build.patch \
    file://pcie-usb/0001-Changes-to-support-kernel-6.6.0.patch \
"

SRC_URI:append:sd-sd = "\
    file://sd-sd/0001-Adapt-Makefile-for-Yocto-build.patch \
    file://sd-sd/0001-Changes-to-support-kernel-6.6.0.patch \
    file://sd-sd/Remove-fw-action-hotplug.patch \
"

S:pcie-usb = "${WORKDIR}/src/pcie-usb/mbt_src"
S:sd-sd = "${WORKDIR}/src/sd-sd/mbt_src"

RDEPENDS_${PN} += "toradex-wifi-config"

COMPATIBLE_MACHINE = "(colibri-imx6ull|colibri-imx8x|verdin-imx8mm|apalis-imx8)"

