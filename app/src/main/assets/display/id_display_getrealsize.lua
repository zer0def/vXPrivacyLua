function after(hook, param)
    local height = param:getSettingInt("display.height", 3100)
    local width = param:getSettingInt("display.width", 1400)
    if height == nil or width == nil then
        return false
    end

    local point = param:getArgument(0)
    if point == nil then
        return false
    end

    point.y = height
    point.x = width
    return true, "N/A", tostring(height) .. "x" .. tostring(width)
end