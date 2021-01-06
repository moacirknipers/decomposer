import React from 'react'
import './styles.css'

const ComboBox =  props => {
    
    const options = props.options !== null && props.options.length > 0 ? props.options : []

    const renderOptions = () => {
        return (
            options.map(option => (
            <option key={option.id} value={option.id} id={option.id}>{option.nome}</option>
            ))
        )

    }

    const changeOption = event => {
        props.onChangeCalculo(event)
    }

    return (
        <div className={`search-box ${props.size}`}>
            <label>{props.descricao}</label>
            <div className="search-box-area">
                <select onChange={changeOption} value={props.selected}>
                    <option value="">Selecione uma opção</option>
                    {renderOptions()}
                </select>
            </div>
        </div>

    )
}

export default ComboBox;