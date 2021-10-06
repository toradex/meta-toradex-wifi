FILESEXTRAPATHS_prepend := "${THISDIR}/files:"

RDEPENDS_${PN} += "toradex-wifi-config"

SRC_URI_append_mfg-mode = " file://0001-wext-Support-old-style-ioctls-for-NXP-Azurewave-Marv.patch "

SRC_URI_append_interface-diversity-pcie-usb = " file://0001-Revert-modules-inherit-TAINT_PROPRIETARY_MODULE.patch "
