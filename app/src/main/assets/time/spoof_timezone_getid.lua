function after(hook, param)
	local res = param:getResult()
	if res == nil then
	    return false
	end

	local fake = param:getSettingReMap("zone.timezone.id", "zone.timezoneid", "Atlantic/Reykjavik")
	param:setResult(fake)
	return true, res, fake
end