
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
	if action == "android.bluetooth.adapter.action.STATE_CHANGED" then
	    log("Bluetooth Adapter State Changed Intent...")
	    local bundle = intent:getExtras()

	    local setting = param:getSetting("bluetooth.state")
	    if setting == nil then
	        return false
	    end

	    local number = tonumber(setting)
	    if number == nil then
	        number = 10 --off
	    end

	    log("Changing Bluetooth state to=" .. number)

	    local st = "android.bluetooth.adapter.extra.STATE"
        bundle:remove(st)
        bundle:putInt(st, number)
		return true, old_intent, tostring(intent)
	end

	return false
end