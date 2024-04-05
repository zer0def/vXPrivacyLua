function after(hook, param)
    local filtered = param:filterSettingsSecure("advertising_id")
    if filtered == true then
        log("ADVERTISING ID SPOOFED")
        return true
    end

	return false
end