package com.kira.learning.utils.image.worker

import com.kira.learning.utils.image.agent.AgentContainer
import com.kira.learning.utils.image.data.ResultData

abstract class BaseWorker<Params, Result: ResultData>(val container: AgentContainer, val params: Params) :
    Worker<Result>
