function after(hook, param)
	local ret = param:getResult()
	if ret == nil then 
		return false
	end

    log("BluetoothAdapter Intercepted")
    param:setResult(null)
    return true
end