function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		ret = 0
	end

	local fake = 5
	local state = param:getSetting("phone.simstate")
	if state ~= nil and tonumber(state) ~= nil then
		fake = tonumber(state)
	else
		if ret == fake then
			fake = 0
		end
	end

	--SIM_STATE_ABSENT=1
	--SIM_STATE_CARD_IO_ERROR=8
	--SIM_STATE_CARD_RESTRICTED=9
	--SIM_STATE_NETWORK_LOCKED=4
	--SIM_STATE_NOT_READY=6
	--SIM_STATE_PERM_DISABLED=7
	--SIM_STATE_PIN_REQUIRED=2
	--SIM_STATE_PUK_REQUIRED=3
	--SIM_STATE_READY=5
	--SIM_STATE_UNKNOWN=0

	param:setResult(fake)
	return true, tostring(ret), tostring(fake)
end