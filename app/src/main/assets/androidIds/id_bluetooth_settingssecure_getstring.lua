function after(hook, param)
    local filtered = param:filterSettingsSecure("bluetooth_name")
    if filtered == true then
        log("BLUETOOTH ID SPOOFED")
        return true
    end

	return false
end