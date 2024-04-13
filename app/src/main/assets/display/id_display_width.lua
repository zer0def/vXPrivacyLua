function after(hook, param)
    local res = param:getResult()
    if res == nil then
        return false
    end

    local width = param:getSettingInt("display.width", 1400)
    if width == nil then
        return false
    end

    param:setResult(width)
    return true, tostring(res), tostring(width)
end