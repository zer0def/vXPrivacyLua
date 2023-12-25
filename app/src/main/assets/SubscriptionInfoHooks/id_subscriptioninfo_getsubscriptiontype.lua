function after(hook, param)
	local ret = param:getResult()
	if ret == nil then
		ret = 0
	end

	local fake = 1
	--SUBSCRIPTION_TYPE_LOCAL_SIM = 0
	--SUBSCRIPTION_TYPE_REMOTE_SIM = 1
	if ret == 1 then
		fake = 0 
	end

	--SubscriptionManager#SUBSCRIPTION_TYPE_LOCAL_SIM or SubscriptionManager#SUBSCRIPTION_TYPE_REMOTE_SIM
	--getSubscriptionType = 
	param:setResult(fake)
	return true, tostring(ret), tostring(fake)
end