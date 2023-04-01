package com.colombia.credit.util.image.worker

import com.colombia.credit.util.image.agent.AgentContainer
import com.colombia.credit.util.image.data.ResultData

abstract class BaseWorker<Params, Result: ResultData>(val container: AgentContainer, val params: Params) :
    Worker<Result>
