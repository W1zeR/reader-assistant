import { useSession } from "next-auth/react";
import { useEffect } from "react";

const RefreshTokenHandler = (props) => {
  const { data: session } = useSession();
  const refreshTokenBeforeExpiryTime = 30 * 60 * 1000;
  const millisInOneSecond = 1000;

  useEffect(() => {
    if (!!session) {
      // We did set the token to be ready to refresh after 23 hours, here we set interval of 23 hours 30 minutes.
      const timeRemaining = Math.round((((session.accessTokenExpiry - refreshTokenBeforeExpiryTime)
        - Date.now()) / millisInOneSecond));
      props.setInterval(timeRemaining > 0 ? timeRemaining : 0);
    }
  }, [props, refreshTokenBeforeExpiryTime, session]);

  return null;
};

export default RefreshTokenHandler;
