function after(hook, param)
    local fake = param:getSettingReMap("unique.android.id", "value.android_id", "0000000000000000")
    local filtered = param:filterSettingsSecure("android_id", fake)
    if filtered == true then
        log("AID")
        return true, "N/A", "Spoofed"
    end
	return false
end