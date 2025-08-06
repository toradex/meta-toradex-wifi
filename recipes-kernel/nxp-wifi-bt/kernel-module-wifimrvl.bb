SUMMARY = "Kernel loadable module for NXP WiFi chip"
LICENSE = "CLOSED"

inherit module

RPROVIDES:${PN}:append:interface-diversity-sd-sd = " kernel-module-sdxxx kernel-module-mlan "
RPROVIDES:${PN}:append:interface-diversity-sd-uart= " kernel-module-moal kernel-module-mlan "
RPROVIDES:${PN}:append:interface-diversity-pcie-usb = " kernel-module-pciexxx kernel-module-mlan "

KERNEL_MODULE_WIFI_INTERFACE:interface-diversity-sd-sd = "sdxxx"
KERNEL_MODULE_WIFI_INTERFACE:interface-diversity-sd-uart = "moal"
KERNEL_MODULE_WIFI_INTERFACE:interface-diversity-pcie-usb = "pciexxx"

KERNEL_MODULE_PROBECONF:append:interface-diversity-sd-uart= " ${KERNEL_MODULE_WIFI_INTERFACE} "
# this firmware is being renamed on nxp-wifi-bt-firmware.bb, make sure the correct file is loaded
module_conf_moal:interface-diversity-sd-uart= "options moal cal_data_cfg=nxp/cal_data.conf cfg80211_wext=12 fw_name=nxp/sdiouart8997_combo_v4_proprietary.bin"
module_conf_moal:interface-diversity-sd-uart:mfg-mode = "options moal cal_data_cfg=none cfg80211_wext=0xf mfg_mode=1 fw_name=nxp/sdio8997_uart_combo_proprietary.bin"

KERNEL_MODULE_PROBECONF:append:interface-diversity-sd-sd = " ${KERNEL_MODULE_WIFI_INTERFACE} "
module_conf_sdxxx:interface-diversity-sd-sd = "options sdxxx cal_data_cfg=nxp/cal_data.conf cfg80211_wext=12"
module_conf_sdxxx:interface-diversity-sd-sd:mfg-mode = "options sdxxx cal_data_cfg=none cfg80211_wext=0xf mfg_mode=1 fw_name=nxp/sdio8997_sdio_combo.bin"

KERNEL_MODULE_PROBECONF:append:interface-diversity-pcie-usb = " ${KERNEL_MODULE_WIFI_INTERFACE} "
module_conf_pciexxx:interface-diversity-pcie-usb = "options pciexxx cal_data_cfg=nxp/cal_data.conf cfg80211_wext=12"
module_conf_pciexxx:interface-diversity-pcie-usb:mfg-mode = "options pciexxx cal_data_cfg=none cfg80211_wext=0xf mfg_mode=1 fw_name=nxp/pcie8997_usb_combo.bin"

SRC_URI:interface-diversity-pcie-usb = "\
    file://cal_data.conf \
    file://0001-Change-Makefile-to-be-compatible-with-yocto-build.patch \
    file://0001-Fix-sa_data-structure-fields.patch\
"

SRC_URI:interface-diversity-pcie-usb:mfg-mode = "\
    file://cal_data.conf \
    file://0001-Change-Makefile-to-be-compatible-with-yocto-build.patch \
"

SRC_URI:interface-diversity-sd-sd = "\
    file://cal_data.conf \
    file://0001-makefile.patch \
    file://0001-Remove-REGULATORY_IGNORE_STALE_KICKOFF-flag.patch\
    file://0001-Update-minimum-version-for-sa_data_min-patch.patch\
    file://0001-Fix-sa_data-structure-fields.patch\
"

SRC_URI:interface-diversity-sd-uart = "\
    file://cal_data.conf \
    file://0001-Replace-KERNELDIR-for-KERNEL_SRC.patch\
    file://0002-add-install-target.patch\
    file://0001-Fix-sa_data-structure-fields.patch\
"

S = "${WORKDIR}/wlan_src"

DEPENDS += "bc-native"
RDEPENDS_${PN} += "toradex-wifi-config"

do_install:append() {
    install -d ${D}${base_libdir}/firmware/nxp
    install -m 0644 ${WORKDIR}/cal_data.conf ${D}${base_libdir}/firmware/nxp
}

FILES:${PN} += "${base_libdir}/firmware/nxp"

COMPATIBLE_MACHINE = "(colibri-imx6ull|colibri-imx8x|verdin-imx8mm|verdin-imx8mp|apalis-imx8)"

addtask nxp_driver_unpack before do_patch after do_unpack
do_nxp_driver_unpack() {
    :
}

SRC_URI:append:interface-diversity-sd-uart = " ${NXP_PROPRIETARY_DRIVER_LOCATION}/${NXP_PROPRIETARY_DRIVER_FILENAME};name=sd-uart-driver;subdir=archive.sd-uart "
SRC_URI[sd-uart-driver.sha256sum] = "${NXP_PROPRIETARY_DRIVER_SHA1}"
do_nxp_driver_unpack:interface-diversity-sd-uart() {
    DIRNAME=$(echo ${NXP_PROPRIETARY_DRIVER_FILENAME} | sed 's/\.zip//')
    DRVNAME=$(basename ${NXP_PROPRIETARY_DRIVER_FILENAME} | sed 's/zip/tar/')
    tar --strip-components=1 -C ${WORKDIR}/archive.sd-uart/ -xf ${WORKDIR}/archive.sd-uart/$DIRNAME/$DRVNAME
    for i in `ls ${WORKDIR}/archive.sd-uart/*-src.tgz`; do
        tar --strip-components=1 -C ${WORKDIR} \
            -xf $i
    done
}

SRC_URI:append:interface-diversity-sd-sd = " ${NXP_PROPRIETARY_DRIVER_LOCATION}/${NXP_PROPRIETARY_DRIVER_FILENAME};name=sd-sd-driver;subdir=archive.sd-sd "
SRC_URI[sd-sd-driver.sha256sum] = "${NXP_PROPRIETARY_DRIVER_SHA1}"
do_nxp_driver_unpack:interface-diversity-sd-sd() {
    DRVNAME=$(basename ${NXP_PROPRIETARY_DRIVER_FILENAME} | sed 's/zip/tar/')
    DIRNAME=$(echo ${NXP_PROPRIETARY_DRIVER_FILENAME} | sed 's/\.zip//')
    tar -C ${WORKDIR}/archive.sd-sd/ -xf ${WORKDIR}/archive.sd-sd/$DIRNAME/$DRVNAME
    for i in `ls ${WORKDIR}/archive.sd-sd/*-src.tgz`; do
        tar --strip-components=1 -C ${WORKDIR} \
            -xf $i
    done
}

SRC_URI:append:interface-diversity-pcie-usb = " ${NXP_PROPRIETARY_DRIVER_LOCATION}/${NXP_PROPRIETARY_DRIVER_FILENAME};name=pcie-usb-driver;subdir=archive.pcie-usb "
SRC_URI[pcie-usb-driver.sha256sum] = "${NXP_PROPRIETARY_DRIVER_SHA1}"
do_nxp_driver_unpack:interface-diversity-pcie-usb() {
    DIRNAME=$(echo ${NXP_PROPRIETARY_DRIVER_FILENAME} | sed 's/\.zip//'| sed 's/NXP_L-//')
    for i in `ls ${WORKDIR}/archive.pcie-usb/$DIRNAME/*-src.tgz`; do
        tar --strip-components=1 -C ${WORKDIR} \
            -xf $i
    done
}

