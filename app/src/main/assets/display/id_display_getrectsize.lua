function after(hook, param)
    local arg1 = param:getArgument(0)
    if arg1 == nil then
        return false
    end

    arg1.left = 0;
    arg1.top = 0;
    -- Bottom-right corner is defined by width and height
    local leftSize = param:getSettingInt("display.width", 1300)
    local bottomSize = param:getSettingInt("display.height", 3100)
    if leftSize == nil or bottomSize == nil then
        return false
    end

    arg1.right = arg1.left + leftSize
    arg1.bottom = arg1.top + bottomSize
    return true, "N/A",  tostring(bottomSize) .. "x" .. tostring(leftSize)
end