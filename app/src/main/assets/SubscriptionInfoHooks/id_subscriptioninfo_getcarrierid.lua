function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		return false
	end

    --gsm.operator.id
    --274+299
    local fake = 573
    local mcc = param:getSetting("gsm.operator.mcc", "274")
    local mnc = param:getSetting("gsm.operator.mnc", "299")

    local mccNumber = tonumber(mcc)
    local mncNumber = tonumber(mnc)
    if mccNumber ~= nil and mncNumber ~= nil then
        fake = mccNumber + mncNumber
    else
        --274+299
        local oId = param:getSetting("gsm.operator.id", "573")
        local oIdNumber = tonumber(oId)
        if oIdNumber ~= nil then
            fake = oIdNumber
        end
    end

	param:setResult(fake)
	return true, tostring(ret), tostring(fake)
end