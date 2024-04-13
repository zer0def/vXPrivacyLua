function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		ret = 0
	end

    local fakeStr = param:getSetting("gsm.sim.type", "1")
    local fake = tonumber(fakeStr)
    if fake == nil then
        return false
    end

    if fake ~= 0 and fake ~= 1 then
        if ret == 1 then
            fake = 0
        end
    end

	--SUBSCRIPTION_TYPE_LOCAL_SIM = 0
	--SUBSCRIPTION_TYPE_REMOTE_SIM = 1

	--SubscriptionManager#SUBSCRIPTION_TYPE_LOCAL_SIM or SubscriptionManager#SUBSCRIPTION_TYPE_REMOTE_SIM
	--getSubscriptionType = 
	param:setResult(fake)
	return true, tostring(ret), tostring(fake)
end