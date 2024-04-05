function after(hook, param)
	local intent = param:getResult()
	if intent == nil then
		return false
	end

	local action = intent:getAction()
	if action == nil then
		return false
	end

	local old_intent = tostring(intent)
	if action == "android.intent.action.BATTERY_CHANGED" or action == "BATTERY_CHANGED" then
		local bmn = luajava.bindClass("android.os.BatteryManager")
		local bundle = intent:getExtras()
		--local eLevel = bmn.EXTRA_LEVEL

        local scale = bundle:getInt(bmn.EXTRA_SCALE, -1)
        if bundle:getInt(bmn.EXTRA_LEVEL, -1) ~= scale and scale >= 0 then
          bundle:putInt(bmn.EXTRA_LEVEL, math.floor(.95*scale))
        end

		--if bundle:getInt(eLevel, -1) then
		--	bundle:putInt(eLevel, math.floor(.95*scale))
		--end

		bundle:remove(bmn.EXTRA_BATTERY_LOW)
		bundle:putInt(bmn.EXTRA_VOLTAGE, 4200)
		bundle:putInt(bmn.EXTRA_TEMPERATURE, 250)
		bundle:putInt(bmn.EXTRA_CHARGE_COUNTER, 0)
		bundle:putInt(bmn.EXTRA_STATUS, bmn.STATUS_GOOD)
		bundle:putInt(bmn.EXTRA_PLUGGED, 0)

		intent:replaceExtras(bundle)

		log("[BATTERY_CHANGED]")
		return true, old_intent, tostring(intent)
	elseif action == "android.intent.action.ACTION_POWER_CONNECTED" then
		intent:setAction("android.intent.action.ACTION_POWER_DISCONNECTED")
		log("[ACTION_POWER_CONNECTED] => [ACTION_POWER_DISCONNECTED]")
		return true, old_intent, tostring(intent)
	elseif action == "android.intent.action.BATTERY_LOW" then
		intent:setAction("android.intent.action.BATTERY_OKAY")
		log("[BATTERY_LOW] => [BATTERY_OKAY]")
		return true, old_intent, tostring(intent)
	end

	return false
end