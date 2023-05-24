import styled from 'styled-components';

// constant
import { COLOR } from '../../constants';

const { subFontColor } = COLOR;

export const Container = styled.section`
  border-top: 2px solid ${subFontColor};
  margin-top: 3rem;
  display: flex;
  flex-direction: column;
  justify-content: center;
  font-size: 3rem;
  padding: 2rem;
  img {
    max-width: 100%;
  }
`;
