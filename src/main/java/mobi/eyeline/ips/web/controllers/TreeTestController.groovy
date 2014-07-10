package mobi.eyeline.ips.web.controllers

import groovy.transform.CompileStatic
import mobi.eyeline.ips.components.tree.TreeEdge
import mobi.eyeline.ips.components.tree.TreeNode

@CompileStatic
class TreeTestController {

    @SuppressWarnings("GrMethodMayBeStatic")
    TreeNode getTree() {

        TreeNode q1 = new TreeNode('Вопрос 1', 'Текст первого вопроса, возможно очень длинный')
        TreeNode q2 = new TreeNode('Вопрос 2', 'Текст второго вопроса, возможно очень длинный')
        TreeNode q3 = new TreeNode('Вопрос 3', 'Текст третьего вопроса, возможно очень длинный')
        TreeNode q4 = new TreeNode('Вопрос 4', 'Текст четвертого  вопроса, возможно очень длинный')
        TreeNode q5 = new TreeNode('Вопрос 5', 'Текст пятого вопроса, возможно очень длинный')
        TreeNode end = new TreeNode('Конец опроса', null)

        q1.edges.with {
            add new TreeEdge('1', 'Длинный текст варианта 1', q2)
            add new TreeEdge('2', 'Длинный текст варианта 2', q3)
            add new TreeEdge('3', 'Длинный текст варианта 3', end)
        }

        q2.edges.with {
            add new TreeEdge('1', 'Длинный текст варианта 1', q4)
            add new TreeEdge('2', 'Длинный текст варианта 2', q4)
            add new TreeEdge('3', 'Длинный текст варианта 3', q4)
        }

        q3.edges.with {
            add new TreeEdge('1', 'Длинный текст варианта 1', q5)
            add new TreeEdge('2', 'Длинный текст варианта 2', q5)
            add new TreeEdge('3', 'Длинный текст варианта 3', q5)
        }

        q4.edges.with {
            add new TreeEdge('1', 'Длинный текст варианта 1', end)
            add new TreeEdge('2', 'Длинный текст варианта 2', end)
            add new TreeEdge('3', 'Длинный текст варианта 3', end)
        }

        q5.edges.with {
            add new TreeEdge('1', 'Длинный текст варианта 1', end)
            add new TreeEdge('2', 'Длинный текст варианта 2', end)
            add new TreeEdge('3', 'Длинный текст варианта 3', end)
        }
        
        return q1
    }
}    

