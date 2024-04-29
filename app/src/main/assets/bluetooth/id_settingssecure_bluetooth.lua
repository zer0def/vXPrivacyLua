function after(hook, param)
    local filtered = param:filterSettingsSecure("bluetooth_name")
    if filtered == true then
        log("BLUETOOTH NAME SPOOFED")
        return true
    end
	return false, "N/A", "Spoofed"
end