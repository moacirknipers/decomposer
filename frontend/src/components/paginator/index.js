import React from 'react';

import './styles.css';

export default function Paginator({ pageOptions, changePage }) {

    return (
        <div className="table-pagination-control mb-3">
            <div className="pagination-item">
                <button type="button" disabled={pageOptions.pageNumber === 0} onClick={() => changePage(0)}>
                    <i className="fa fa-angle-double-left"></i>
                </button>
            </div>
            <div className="pagination-item">
                <button type="button" disabled={pageOptions.pageNumber === 0} onClick={() => changePage(pageOptions.pageNumber - 1)}>
                    <i className="fa fa-angle-left"></i>
                </button>
            </div>
            <div className="pagination-item">
                    {pageOptions.pageNumber + 1}
            </div>
            <div className="pagination-item" >
                <button type="button" disabled={pageOptions.pageNumber + 1 >= pageOptions.totalPages} onClick={() => changePage(pageOptions.pageNumber + 1)}>
                    <i className="fa fa-angle-right"></i>
                </button>
            </div>
            <div className="pagination-item" style={{ borderRight: '0' }}>
                <button type="button" disabled={pageOptions.pageNumber + 1 >= pageOptions.totalPages} onClick={() => changePage(pageOptions.totalPages - 1)}>
                    <i className="fa fa-angle-double-right"></i>
                </button>
            </div>
        </div>
    );
}
