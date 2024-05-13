function after(hook, param)
    local filtered = param:queryFilterAfter("com.google.android.gsf.gservices", "android_id", param:getSetting("unique.gsf.id"))
    if filtered == true then
        log("GSF")
        return true
    end
	return false
end