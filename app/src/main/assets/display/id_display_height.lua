function after(hook, param)
    local res = param:getResult()
    if res == nil then
        return false
    end

    local height = param:getSettingInt("display.height", 3100)
    if height == nil then
        return false
    end

    param:setResult(height)
    return true, tostring(res), tostring(height)
end