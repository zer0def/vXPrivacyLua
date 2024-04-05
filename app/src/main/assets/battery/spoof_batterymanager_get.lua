function after(hook, param)
	local arg = param:getArgument(0)
	local bmn = param:getThis()
	local res = param:getResult()
	local fake = nil

	if arg == bmn.BATTERY_PROPERTY_CHARGE_COUNTER then 
		log("BATTERY_PROPERTY_CHARGE_COUNTER");
		fake = 0
		--Battery capacity in microampere-hours, as an integer.
	elseif arg == bmn.BATTERY_PROPERTY_ENERGY_COUNTER then
		log("BATTERY_PROPERTY_ENERGY_COUNTER");
		fake = 150000
		--Battery remaining energy in nanowatt-hours, as a long integer.
	elseif arg == bmn.BATTERY_PROPERTY_STATUS then
		log("BATTERY_PROPERTY_STATUS")
		fake = bmn.BATTERY_STATUS_DISCHARGING
	--elseif arg == bmn.BATTERY_PLUGGED_USB or arg == bmn.BATTERY_PLUGGED_WIRELESS or bmn.BATTERY_PLUGGED_DOCK or bmn.BATTERY_PLUGGED_AC then
	--	fake = 0
	--use these if your comparing return value as these indicate constant state value
	elseif arg == bmn.BATTERY_PROPERTY_CAPACITY then
		log("BATTERY_PROPERTY_CAPACITY=100")
		fake = 100
	end 

	if fake ~= nil then 
		param:setResult(fake)
		return true
	end

	return false
end