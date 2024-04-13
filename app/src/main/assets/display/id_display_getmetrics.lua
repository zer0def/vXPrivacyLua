function after(hook, param)
    local height = param:getSettingInt("display.height", 3100)
    local width = param:getSettingInt("display.width", 1400)
    if height == nil or width == nil then
        return false
    end

    local displayMetrics = param:getArgument(0)
    if displayMetrics == nil then
        return false
    end

    displayMetrics.heightPixels = height
    displayMetrics.widthPixels = width
    return true, "N/A", tostring(height) .. "x" .. tostring(width)
end