import React from 'react'
import './styles.css'

const VariavelInput = props => {

    const changeValue = event => {
        props.onChange(event)
    }

    return (
        <div className={`input-variavel ${props.size}`}>
            <label>{props.descricao}</label>
            <input className="inputVariavel"
                   type={props.type === 'DECIMAL' || props.type === 'INTEIRO' ? 'number' : 'text'}
                   value={props.value}
                   id={props.descricao}
                   onChange={changeValue}
            />
        </div>
    )

}

export default VariavelInput