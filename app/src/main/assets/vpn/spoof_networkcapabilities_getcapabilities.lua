function after(hook, param)
	local ret = param:getResult()
	if ret == nil then 
		return false
	end

	local arrayClass = luajava.bindClass("java.lang.reflect.Array")
	local intClass = luajava.bindClass("java.lang.Integer")
	local intType = intClass.TYPE

	local lst = luajava.newInstance("java.util.ArrayList")
	for index = ret["length"], 1, -1 do
	    local itm = ret[index]
	    if itm ~= 0x4 then
	        lst:add(itm)
	    end
	end

	--our version of 2array
	local arr = arrayClass:newInstance(intType, lst:size())
    for i = 1, lst:size() - 1 do
        arr[i] = lst:get(i)
    end

    param:setResult(arr)
    return true
end