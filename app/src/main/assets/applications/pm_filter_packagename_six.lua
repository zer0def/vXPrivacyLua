function after(hook, param)
    local res = param:getResult()
    if res == nil then
        return false
    end

    local p = param:getArgument(1)
    if p == nil then
        return false
    end

    local good = param:listHasString("applications.allow.list", p)
    if good == true then
        return false
    end

    local clsFileNotFound = luajava.bindClass("java.lang.SecurityException")
    local fake = luajava.new(clsFileNotFound, p)
    param:setResult(fake)
    return true
end