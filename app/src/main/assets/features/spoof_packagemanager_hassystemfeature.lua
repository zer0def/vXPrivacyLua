function after(hook, param)
    local res = param:getResult()
    if res == nil then
        return false
    end

    local p = param:getArgument(0)
    if p == nil then
        return false
    end

    log("Has System Feature: " .. p)
    param:setResult(false)
    return true
end
