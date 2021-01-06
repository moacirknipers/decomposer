import { combineReducers } from 'redux'
import simulateReducer from './pages/simulate-formulas/reducer'
import listVarsReducer from './pages/list-variables/reducer'

const rootReducer = combineReducers({
    simulate: simulateReducer,
    listVars: listVarsReducer
})

export default rootReducer