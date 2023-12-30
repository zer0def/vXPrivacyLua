function after(hook, param)
	local ret = param:getResult()
	if ret == nil or ret.length == 0 then 
		return false
	end

	local arg = param:getArgument(0)
	if arg == nil or arg ~= "deviceUniqeId" then
	    return false
	end

    --local arr_class = luajava.bindClass("java.lang.reflect.Array")
    --local byt_class = luajava.bindClass("java.lang.Byte")

    local fake = param:createByteArray(ret.length)
    log("DMR Setting Empty Array")
    param:setResultByteArray(fake)



    --local fake = arr_class:newInstance(byt_type, ret.length)
    --param:setResult(fake)
    return true
end