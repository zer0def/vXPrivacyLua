function after(hook, param)
    local res = param:getResult()
    if res == nil then
        return false
    end

    local height = param:getSettingInt("display.height", 3100)
    local width = param:getSettingInt("display.width", 1400)
    if height == nil or width == nil then
        return false
    end

    local old = tostring(res.heightPixels) .. "x" .. tostring(res.widthPixels)
    local new = tostring(height) .. "x" .. tostring(width)

    res.heightPixels = height
    res.widthPixels = width

    param:setResult(res)
    return true, old, new
end