package com.educalab.ninobiologo.domain

import com.educalab.ninobiologo.domain.logic.ExperimentEngine
import com.educalab.ninobiologo.domain.model.Experiment
import com.educalab.ninobiologo.domain.model.ExperimentOutcome
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExperimentEngineTest {

    private val experiment = Experiment(
        id = "exp_luz", environmentId = "bosque_de_vida", order = 1,
        question = "¿Qué pasa si una planta recibe menos luz?", description = "desc",
        variableName = "Horas de luz al día", variableUnit = "horas",
        variableMin = 0, variableMax = 12, idealMin = 6, idealMax = 10, rewardXp = 30
    )

    @Test
    fun `valor dentro del rango ideal no produce cambios`() {
        val result = ExperimentEngine.evaluate(experiment, 8)
        assertEquals(ExperimentOutcome.SIN_CAMBIOS, result.outcome)
    }

    @Test
    fun `valor lejos del rango ideal produce efecto notable`() {
        val result = ExperimentEngine.evaluate(experiment, 0)
        assertEquals(ExperimentOutcome.EFECTO_NOTABLE, result.outcome)
    }

    @Test
    fun `valor apenas fuera del rango produce efecto leve`() {
        val result = ExperimentEngine.evaluate(experiment, 5)
        assertEquals(ExperimentOutcome.EFECTO_LEVE, result.outcome)
    }

    @Test
    fun `valor fuera de los limites se recorta (caso limite)`() {
        val result = ExperimentEngine.evaluate(experiment, 999)
        assertEquals(ExperimentEngine.evaluate(experiment, experiment.variableMax).outcome, result.outcome)
    }

    @Test
    fun `variable muy alejada del ideal en un rango ideal estrecho produce efecto drastico`() {
        val narrowExperiment = experiment.copy(idealMin = 8, idealMax = 9)
        val result = ExperimentEngine.evaluate(narrowExperiment, 0)
        assertEquals(ExperimentOutcome.EFECTO_DRASTICO, result.outcome)
    }

    @Test
    fun `xp otorgada nunca es negativa`() {
        val result = ExperimentEngine.evaluate(experiment, 0)
        assertTrue(result.xpAwarded >= 0)
    }

    @Test
    fun `resultado ideal otorga la xp mas alta`() {
        val ideal = ExperimentEngine.evaluate(experiment, 8)
        val lejos = ExperimentEngine.evaluate(experiment, 0)
        assertTrue(ideal.xpAwarded > lejos.xpAwarded)
    }
}
