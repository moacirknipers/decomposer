import React, { useEffect, useState } from 'react';

import './styles.css'

export default function InputValidator({ message, targetProperty}) {

    const [show, setShow] = useState(false);

    return (
        <>
            {show ? (
                <span className="error-area">
                    {message}
                </span>
            ) : (null)}
        </>

    );
}
