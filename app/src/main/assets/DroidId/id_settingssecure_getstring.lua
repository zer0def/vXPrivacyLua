function after(hook, param)
    local filtered = param:filterSettingsSecure("android_id")
    if filtered == true then
        log("ANDROID ID SPOOFED")
        return true
    end

	return false
end