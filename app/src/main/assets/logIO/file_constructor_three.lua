function before(hook, param)
    local path = '[F]'
    local argOne = param:getArgument(0)
    local argTwo = param:getArgument(1)
    if argOne ~= nil then
        path = path .. ' P=' .. argOne:getAbsolutePath()
    end

    if argTwo ~= nil then
        path = path .. ' C=' .. argTwo
    end


    log('[xlog] File being Constructed File(File, String) => ' .. path)
    return false
end