import React, { Component } from 'react';
import ReactModal from 'react-modal';
import { connect } from 'react-redux';
import { bindActionCreators } from 'redux';
import {Route} from 'react-router-dom'

import ComboBox from '@/components/combobox'
import TableFormulas from '@/components/table-formulas'
import VariavelInput from '@/components/variavel-input'
import { variavelValueChange, openCloseModal, loadFormulas, execute, loadVariaveis, clearAll, loadGrupos, filterModalFormulas } from './actions'

import './styles.css'
import Paginator from '@/components/paginator';

class SimulateFormulas extends Component {

    constructor(props) {
        super(props)

        this.state = {searchDescricao: '', searchGrupo: ''}
    }

    componentWillMount() {
        this.props.loadFormulas()
        this.props.loadGrupos()        
    }

    selectFormula = (e) => {
        const id = e.target !== undefined ? e.target.value : e
        if (id !== undefined && id !== null) {
            this.props.clearAll()
            if (id !== "") {
                this.props.loadVariaveis(id)
            }
        }
    }

    getComposicao = (composicao) => {
        var result = ''
        Object.keys(composicao).map((i, index) => (
            result = result + composicao[i].variavel + " = " + composicao[i].formula + "\n"
        ))
        return result
    }

    getResultado = (resultado) => {
        var result = ''
        Object.keys(resultado).map((i, index) => (
            result = result + resultado[i].nome + " = " + resultado[i].calculatedValue + "\n"
        ))
        return result
    }

    searchDescricaoChange(event) {
        this.setState({searchDescricao: event.target.value})
      }

    searchGrupoChange(event) {
        this.setState({searchGrupo: event.target.value})
      }  

    sendSearch = (page) => {
        var filter = []
        if (page !== null) {
            filter.page = page
        } else {
            filter.page = 0
        }
        filter.size = 5
        filter.descricao = this.state.searchDescricao
        filter.tipoDeCalculo = this.state.searchGrupo
        this.props.filterModalFormulas(filter)
    }


    render() {
        const { variavelValueChange, variaveis, openCloseModal, execute } = this.props

        return (
            <>
            <div className="column mt-3">
                <h5>Simulador de Cálculo</h5>
                <hr></hr>
                <div className="row">
                    <ComboBox selected={this.props.selected} size="col-md-3" descricao="Fórmula" options={this.props.formulas} value='' onChangeCalculo={(e) => { this.selectFormula(e)}} />
                    <button onClick={() => openCloseModal(true)}><i className="fa fa-search lupa"></i></button>
                </div>
                <br />
                <div className="row">
                    <div className="col-md-12 text-area">
                        <label>Composição do cálculo</label>
                        <textarea placeholder="Selecione uma fórmula."
                            value={this.getComposicao(this.props.formulasComposicao)}
                            id="formulaInput"
                        />
                    </div>
                </div>
                <br />
                <h6>Variáveis</h6>
                <div className="row">
                    { Object.keys(variaveis).map((variavel, index) => (
                            <VariavelInput value={variaveis[variavel].value} onChange={variavelValueChange} key={variaveis[variavel].nome} descricao={variaveis[variavel].nome} type={variaveis[variavel].tipoDado} size="col-md-3"/>)
                      )}
                </div>
                <br/>
                <div className="row">
                    <button type="button" className="btn-execute" onClick={() => execute(this.props.selected, this.props.formulas, variaveis)}>Executar</button>
                </div>
                <br/>
                <label>Resultado</label>
                <div className="row">
                    <div className="col-md-12">
                        <textarea placeholder="Resultado..." disabled
                            value={this.getResultado(this.props.result)}
                            id="resultadoInput"
                        />
                    </div>
                </div>
                <div className="btns-area">
                    <div className="row">
                    <Route render={({ history}) => (<button className="btn-back" type="button" onClick={() => { history.push('/') }}> Voltar </button>)} />
                    </div>
                </div>
            </div>

            <ReactModal isOpen={this.props.showModal} style={{
              overlay: {
              },
              content: {
                color: 'lightsteelblue',
                width: 800,
                height: 600,
                margin: '0 auto'
              }
            }}
            onAfterOpen={() => this.props.filterModalFormulas(null)}
            >
                <div className="row float-right">
                    <button onClick={() => openCloseModal(false)}><i className="fa fa-times modal-btn-close"></i></button>
                </div>
                <div className="row">
                        <div className="col-md-5">
                            <label>Descrição</label>
                            <input className="modalDescricao" type="text" value={this.state.searchDescricao} onChange={this.searchDescricaoChange.bind(this)}
                            />
                        </div>
                        <ComboBox size="col-md-3" descricao="Grupo" options={this.props.tiposCalculo} value={this.state.searchGrupo} onChangeCalculo={this.searchGrupoChange.bind(this)} />
                        <button type="button" className="btn-search" onClick={() => this.sendSearch(0)}>Pesquisar</button>
                </div>
                <div className="row">
                    <TableFormulas listFormulas={this.props.modalFormulas} buttonMethod={this.selectFormula}/>
                </div>
                <div className="row">
                    <Paginator pageOptions={this.props.paginacaoModal} changePage={this.sendSearch}/>
                </div>
            </ReactModal>
            </>
        )
    }
}

const mapStateToProps = state => ({
    variaveis: state.simulate.variaveis, 
    showModal: state.simulate.showModal, 
    formulas: state.simulate.formulas,
    formulasComposicao: state.simulate.formulasComposicao,
    result: state.simulate.result,
    selected: state.simulate.selected,
    tiposCalculo: state.simulate.tiposCalculo,
    modalFormulas: state.simulate.modalFormulas,
    paginacaoModal: state.simulate.paginacaoModal
})

const mapDispatchToProps = dispatch => bindActionCreators({ variavelValueChange, openCloseModal, loadFormulas, execute, loadVariaveis, clearAll, loadGrupos, filterModalFormulas }, dispatch)

export default connect(mapStateToProps, mapDispatchToProps)(SimulateFormulas)