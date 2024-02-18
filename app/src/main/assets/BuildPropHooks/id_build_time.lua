function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

    local fake = param:getSetting("android.build.date.utc")
    if fake == nil then
        --fake = "1609459200000"
        fake = "0"
    end

    log("Spoofing Build.TIME => 0")
    param:setResultToLong(fake)
    --param:setResult(fake)
    return true
end