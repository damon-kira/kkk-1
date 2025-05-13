package com.kira.learning.util.image.worker

import com.kira.learning.util.image.agent.AgentContainer
import com.kira.learning.util.image.data.ResultData

abstract class BaseWorker<Params, Result: ResultData>(val container: AgentContainer, val params: Params) :
    Worker<Result>
