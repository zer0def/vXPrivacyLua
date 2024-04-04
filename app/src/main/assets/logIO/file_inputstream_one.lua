function before(hook, param)
    local arg = param:getArgument(0)
    if arg == nil then
        return false
    end

    log('[xlog] File being Streamed FileInputStream(String) => ' .. arg)
    return false
end