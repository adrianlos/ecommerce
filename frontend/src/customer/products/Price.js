import React from 'react';

export default function Price(props) {
    return (
        <em style={props.style}>{Number(props.value).toFixed(2)} zł</em>
    );
}