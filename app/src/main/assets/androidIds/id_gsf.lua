function after(hook, param)
    local filtered = param:queryFilterAfter("gsf_id")
    if filtered == true then
        log("GOOGLE SERVICES FRAMEWORK ID")
        return true
    end

	return false
end