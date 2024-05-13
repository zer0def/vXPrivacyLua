function after(hook, param)
    local fake = param:getSettingReMap("unique.bluetooth.address", "bluetooth.id", "00:00:00:00:00:00")
    local filtered = param:filterSettingsSecure("bluetooth_name", fake)
    if filtered == true then
        log("BTH Mac")
        return true, "N/A", "Spoofed"
    end
	return false
end