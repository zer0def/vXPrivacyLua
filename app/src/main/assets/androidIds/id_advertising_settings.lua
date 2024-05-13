function after(hook, param)
    local fake = param:getSettingReMap("unique.google.advertising.id", "ad.id", "84630630-u4ls-k487-f35f-h37afe0pomwq")
    local filtered = param:filterSettingsSecure("advertising_id", fake)
    if filtered == true then
        log("ADID")
        return true, "N/A", "Spoofed"
    end
	return false
end