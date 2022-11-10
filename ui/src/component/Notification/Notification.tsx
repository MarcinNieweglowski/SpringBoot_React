import classes from './Notification.module.css';
import {NotificationStatus} from "../../store/NotificationStatus";

const Notification = (props: any) => {
  let specialClasses = classes.success;

  if (props.status === NotificationStatus.ERROR) {
    specialClasses = classes.error;
  }

  if (props.status === NotificationStatus.PENDING) {
    specialClasses = classes.pending;
  }

  const cssClasses = `rounded ${classes.animate} ${classes.notification} ${specialClasses}`;

  return (
    <div className={classes.flashyize}>
      <section className={cssClasses}>
        <h2>{props.title}</h2>
        <p>{props.message}</p>
      </section>
    </div>
  );
};

export default Notification;