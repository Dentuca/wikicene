package wikicene.exception

import wikicene.api.SupportedParam

class MissingParamException(param: SupportedParam) : WikiceneException(
    "Param ${param.param} was not provided"
)
